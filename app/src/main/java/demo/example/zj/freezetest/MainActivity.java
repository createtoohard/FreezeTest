package demo.example.zj.freezetest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "FreezeTest";
    private static final String MyAction = "hehehe";
    private Switch normalAlarm;
    private Switch wakeupAlarm;
    private Switch sendBroadcast;
    private Switch requestNetwork;
    private Button start;
    private boolean normalAlarmIsChecked = false;
    private boolean wakeupAlarmIsChecked = false;
    private boolean sendBroadcastIsChecked = false;
    private boolean requestNetworksChecked = false;
    private AlarmManager mAlarmManager;
    private BroadcastReceiver mBoradcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction(MyAction);
        intentFilter.addAction("gegege");
        mBoradcastReceiver = new MyBroadcast();
        registerReceiver(mBoradcastReceiver, intentFilter);
        setContentView(R.layout.activity_main);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        initView();

    }

    private void initView() {
        normalAlarm = (Switch) findViewById(R.id.normal_alarm);
        normalAlarm.setOnClickListener(this);
        wakeupAlarm = (Switch) findViewById(R.id.wakeup_alarm);
        wakeupAlarm.setOnClickListener(this);
        sendBroadcast = (Switch) findViewById(R.id.send_broadcast);
        sendBroadcast.setOnClickListener(this);
        requestNetwork = (Switch) findViewById(R.id.request_network);
        sendBroadcast.setOnClickListener(this);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "START ...");
                processSomething();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_alarm:
                Log.e(TAG, "normal alarm click");
                if(normalAlarm.isChecked()) {
                    Log.e(TAG, "normal_alarm is checked");
                    normalAlarmIsChecked = true;
                } else {
                    Log.e(TAG, "normal_alarm is not checked");
                    normalAlarmIsChecked = false;
                }

                break;
            case R.id.wakeup_alarm:
                Log.e(TAG, "wakeup_alarm click");
                if(wakeupAlarm.isChecked()) {
                    Log.e(TAG, "wakeup_alarm is checked");
                    wakeupAlarmIsChecked = true;
                } else {
                    Log.e(TAG, "wakeup_alarm is not checked");
                    wakeupAlarmIsChecked = false;
                }
                break;
            case R.id.send_broadcast:
                Log.e(TAG, "send_broadcast click");
                if(sendBroadcast.isChecked()) {
                    sendBroadcastIsChecked = true;
                    Log.e(TAG, "send_broadcast is checked");
                } else {
                    Log.e(TAG, "send_broadcast is not checked");
                    sendBroadcastIsChecked = false;
                }
                break;
            case R.id.request_network:
                Log.e(TAG, "request_network click");
                if(requestNetwork.isChecked()) {
                    requestNetworksChecked = true;
                    Log.e(TAG, "request_network is checked");
                } else {
                    Log.e(TAG, "request_network is not checked");
                    requestNetworksChecked = false;
                }
                break;
        }
    }

    private void processSomething() {
        Log.e(TAG, "processSomething");
        Intent mIntent = new Intent(MyAction);
        Intent mAlarmIntent = new Intent("gegege");
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent, 0);
        if(normalAlarmIsChecked) {
            Log.e(TAG, "set normal alarm");
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME, 60*1000, mPendingIntent);
        }
        if(wakeupAlarmIsChecked) {
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000*60, mPendingIntent);
        }
        if(sendBroadcastIsChecked) {
            sendBroadcast(mIntent);
        }
        if(requestNetworksChecked) {

        }
    }

    public class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyAction)) {
                Log.e(TAG, "receiver broadcast");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestNet();
                    }
                }).start();

            } else {
                sendBroadcast(new Intent("hehehe"));
            }
        }
    }

    private void requestNet() {
        Log.e(TAG, "request network ing");
        try {
            // 新建一个URL对象
            URL url = new URL("https://www.baidu.com");
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                Log.e(TAG, "Get方式请求成功，result--->");
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBoradcastReceiver);
    }
}
