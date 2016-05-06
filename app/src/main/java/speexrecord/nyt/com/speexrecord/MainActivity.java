package speexrecord.nyt.com.speexrecord;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startrecordButton;
    private Button stoprecordButton;
    private Button playrecordButton;
    private Button finishButton;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zgtxRecord";
    private File mFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFile();
        initViews();
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
                this.setTitle("开始录音了");
                break;
            case R.id.stop_record:
                this.setTitle("录音停止");
                break;
            case R.id.play_record:
                this.setTitle("开始播放");
                break;
            case R.id.finish:
                break;
        }
    }
}
