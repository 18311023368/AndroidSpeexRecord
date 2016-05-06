package speexrecord.nyt.com.speexrecord;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @作者：聂钰谭
 * @创建日期： 2016/5/6 17:02
 * @实现功能：
 * @更改日志：
 */
public class RecordSpeexPlayRunnable implements Runnable {
    private String mFileName;
    private AudioTrack mAudioTrak = null;

    public RecordSpeexPlayRunnable(String fileName) {
        this.mFileName = fileName;
    }

    @Override
    public void run() {
        mAudioTrak = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, RecordSpeexRunnable.bufferSizeInBytes, AudioTrack.MODE_STREAM);
        File file = new File(mFileName);
        int recordLenth = (int) file.length();
        byte[] readsize = new byte[recordLenth];
        try {
            FileInputStream fis=new FileInputStream(file);
            BufferedInputStream bis=new BufferedInputStream(fis);
            DataInputStream dis=new DataInputStream(bis);
            int i = 0;
            while(bis.available()>0){
                readsize[i] = (byte) dis.readByte();
                i++;
            }
            bis.close();
            mAudioTrak.play();
            mAudioTrak.write(readsize,0,recordLenth);
            mAudioTrak.stop();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
