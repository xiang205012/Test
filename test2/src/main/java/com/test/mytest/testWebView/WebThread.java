package com.test.mytest.testWebView;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/12/29.
 */
public class WebThread extends Thread {

    private static final String TAG = "WebThread-->>";
    private String url;

    public WebThread(String url){
        this.url = url;
    }

    @Override
    public void run() {
        //下载文件
        try {
            Log.i(TAG,"下载开始");
            URL webUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) webUrl.openConnection();
            //接收输入流
            connection.setDoInput(true);
            //发送输出流
            connection.setDoOutput(true);
            InputStream inputStrem = connection.getInputStream();
            FileOutputStream outputStream = null;

            File downloadFile;
            File sdFile;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                downloadFile = Environment.getExternalStorageDirectory();
                sdFile = new File(downloadFile,"test.apk");
                //指定数据写入sdFile
                outputStream = new FileOutputStream(sdFile);
            }
            byte[] buffer = new byte[6 * 1024];
            int lenght;
            while((lenght = inputStrem.read(buffer)) != -1){
                if(outputStream != null)
                outputStream.write(buffer,0,lenght);
            }
            inputStrem.close();
            outputStream.flush();
            outputStream.close();
            Log.i(TAG, "下载完成");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
