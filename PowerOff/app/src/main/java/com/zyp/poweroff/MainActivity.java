package com.zyp.poweroff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /**
     * 显示值
     */
    private TextView mTextView;
    /**
     * 计时器
     */
    private Timer mTimer;
    /**
     * 定时任务
     */
    private TimerTask mTimerTask;
    /**
     * 计时器数值
     */
    private int mTick = 20;
    /**
     * 计时器状态
     */
    private boolean mTickFlag = true;

    /**
     * 定时器执行的Handler
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            setTick();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        mTextView = findViewById(R.id.txt);
        mTextView.setText(String.valueOf(mTick));

        initTimer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (mTickFlag) {
                    stopTimer();
                    mTickFlag = false;
                } else {
                    startTimer();
                    mTickFlag = true;
                }
            }
        });
    }

    /**
     * 初始化定时器
     */
    private void initTimer() {
        mTimer = new Timer();
        mTimerTask = new TickTimerTask();
        startTimer();
    }

    /**
     * 启动定时器
     */
    private void startTimer() {
        mTimer.schedule(mTimerTask, 1000, 1000);//按秒计时
    }

    /**
     * 停止定时器
     */
    private void stopTimer() {
        mTick = 20;
        mTimer.cancel();
    }

    /**
     * 自定义定时器任务
     */
    class TickTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendMessage(new Message());
        }
    }

    private void setTick() {
        try {
            mTick = mTick - 1;
            mTextView.setText(String.valueOf(mTick));
            if (mTick < 0)
                shutDown();
        } catch (Exception e) {
            MessageToast.showToast(this, e.getMessage(), 1000);
        }
    }

    private void shutDown() {
        try {
            //Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"});  //关机
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            out.writeBytes("reboot -p\n");
//            out.writeBytes("poweroff -f\n");//可以为reboot -p\n
            out.writeBytes("exit\n");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            MessageToast.showToast(this, e.getMessage(), 1000);
        }
    }
//    private void shutDown() {
//        // 源码中"android.intent.action.ACTION_REQUEST_SHUTDOWN“ 就是 Intent.ACTION_REQUEST_SHUTDOWN方法
//        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
//        // 源码中"android.intent.extra.KEY_CONFIRM"就是 Intent.EXTRA_KEY_CONFIRM方法,
//        // 其中false换成true,会弹出是否关机的确认窗口
//        intent.putExtra("android.intent.extra.KEY_CONFIRM", true);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//
//    private void shutDown() {
//        try {
//
//            //获得ServiceManager类
//            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
//            //获得ServiceManager的getService方法
//            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
//            //调用getService获取RemoteService
//            Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);
//            //获得IPowerManager.Stub类
//            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
//            //获得asInterface方法
//            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
//            //调用asInterface方法获取IPowerManager对象
//            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
//            //获得shutdown()方法
//            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
//            //调用shutdown()方法
//            shutdown.invoke(oIPowerManager, false, true);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            MessageToast.showToast(this, e.getMessage(), 1000);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}