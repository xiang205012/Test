package com.test.mytest.testCamerahead;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/13.
 */
public class ImageTools {

    /**
     * bitmap压缩
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)width/w);
        float scaleHeight = ((float)height/h);
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
        return newBitmap;
    }

    /**
     * 保存图片到sdCard
     * @param newBitmap
     * @param path
     * @param photoName
     */
    public static void savePhotoToSDCard(Bitmap newBitmap, String path, String photoName) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdir();
            }
            File photoFile = new File(path,photoName + ".png");
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if(newBitmap != null){
                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            }finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * @param path
     * @param fileName
     */
    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File folder = new File(path);
            File[] files = folder.listFiles();
            for(int i = 0;i<files.length;i++){
                if(files[i].getName().equals(fileName)){
                    files[i].delete();
                }
            }
        }



    }
}
