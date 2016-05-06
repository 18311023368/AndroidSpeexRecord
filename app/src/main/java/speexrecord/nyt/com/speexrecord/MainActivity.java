package speexrecord.nyt.com.speexrecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startrecordButton;
    private Button stoprecordButton;
    private Button playrecordButton;
    private Button finishButton;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zgtxRecord2/";//文件夹路径
    private File mFile = null;
    private RecordSpeexRunnable mRecordRunnable = null;
    private String myRecordPath = "";//文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initFile();
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //后台线程执行
    @Subscribe(threadMode = ThreadMode.BackgroundThread)
    public void onMainEventBus(FileBean msg) {
        Log.d("TAG", "msg:" + msg.getFilePath());
        myRecordPath = msg.getFilePath();
    }

    private void initViews() {
        finishButton = (Button) findViewById(R.id.finish);
        playrecordButton = (Button) findViewById(R.id.play_record);
        stoprecordButton = (Button) findViewById(R.id.stop_record);
        startrecordButton = (Button) findViewById(R.id.start_record);
        finishButton.setOnClickListener(this);
        playrecordButton.setOnClickListener(this);
        stoprecordButton.setOnClickListener(this);
        startrecordButton.setOnClickListener(this);

    }

    //创建文件夹
    private void initFile() {
        mFile = new File(filePath);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_record:
                requestPermission();
                break;
            case R.id.stop_record:
                this.setTitle("录音停止");
                mRecordRunnable.startRecord(false);
                break;
            case R.id.play_record:
                this.setTitle("开始播放");
                Thread thread = new Thread(new RecordSpeexPlayRunnable(myRecordPath));
                thread.start();
                break;
            case R.id.finish:
                System.exit(0);
                break;
        }
    }

    /**
     * 以下是获取权限
     */
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    public static final int EXTERNAL_Record_REQ_CODE = 11;

    public void requestPermission() {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQ_CODE);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, EXTERNAL_Record_REQ_CODE);
            }
        } else {
            this.setTitle("开始录音了");
            if (mRecordRunnable == null) {
                mRecordRunnable = new RecordSpeexRunnable(filePath);
                Thread mThread = new Thread(mRecordRunnable);
                mThread.start();
            }
            mRecordRunnable.startRecord(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
            case EXTERNAL_Record_REQ_CODE:
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    this.setTitle("开始录音了");
                    if (mRecordRunnable == null) {
                        mRecordRunnable = new RecordSpeexRunnable(filePath);
                        Thread mThread = new Thread(mRecordRunnable);
                        mThread.start();
                    }
                    mRecordRunnable.startRecord(true);
                } else {
                    //申请失败，可以继续向用户解释。
                }

                break;
        }
    }

}
