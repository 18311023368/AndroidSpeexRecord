package speexrecord.nyt.com.speexrecord;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * @作者：聂钰谭
 * @创建日期： 2016/5/6 10:07
 * @实现功能：子线程录音
 * @更改日志：
 */
public class RecordSpeexRunnable implements Runnable, RecordInterface {
    private String mFilePath = "";//录制路径
    public static int audioSource = MediaRecorder.AudioSource.MIC;//音频来源
    public static int sampleRateInHz = 8000;//采样率
    public static  int channelConfig = AudioFormat.CHANNEL_IN_MONO;//声道
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;//编码制式和采样大小
    public static int bufferSizeInBytes;//采集数据需要的缓冲区的大小，
    private volatile  boolean isRecording = false;//是否正在录制
    private int writeSize = 1024;
    private FileOutputStream fos;
    private final Object mutex = new Object();

    public RecordSpeexRunnable(String filePath) {
        this.mFilePath = filePath + UUID.randomUUID().toString() + ".pcm";
        EventBus.getDefault().post(new FileBean(mFilePath));
    }

    @Override
    public void run() {

        synchronized (mutex) {//等待唤醒
            while (!this.isRecording) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException("Wait() interrupted!", e);
                }
            }
        }

        int bufferRead = 0;
        // new一个byte数组用来存一些字节数据，
        byte[] bufferWrite = new byte[writeSize];
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        AudioRecord mAudiRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        mAudiRecord.startRecording();
        //创建文件
        File recordFile = new File(mFilePath);
        try {
            fos = new FileOutputStream(recordFile);
            while (isRecording) {
                bufferRead = mAudiRecord.read(bufferWrite, 0, writeSize);
                if (AudioRecord.ERROR_INVALID_OPERATION != bufferRead) {
                    Log.d("TAG", "bufferReadsize:" + bufferRead);
                    fos.write(bufferWrite, 0, bufferRead);//写入文件
                }
            }
            fos.close();
            mAudiRecord.stop();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void startRecord(boolean isStart) {
        synchronized (mutex){
            isRecording = isStart;
            if (this.isRecording) {
                mutex.notify();
            }
        }

    }

    public boolean isRecording() {
        synchronized (mutex) {
            return isRecording;
        }
    }






}
