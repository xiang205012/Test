package cj.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import cj.library.MainActivity;

/**
 * Created by cj on 2015/12/10.
 */
public class BitmapUtils {

    /**
     * 图片压缩到指定大小(单位：kb)
     * @param activity   上下文
     * @param srcPath    图片地址
     * @param size       指定大小(50 ~ 100)
     * @param localPath  保存的路径，如果不保存设置成null
     *  return 返回 Bitmap
     */
    @SuppressLint("SdCardPath")
    public static Bitmap compressBitmapMemorySize(Activity activity,String srcPath,int size,String localPath) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int bsize = 0;
        if (w <= ww && h <= hh) {
            bsize = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            bsize = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = bsize;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > size * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
            quality -= 20;
            System.out.println(baos.toByteArray().length);
        }
        try {
            if(!(localPath == null)) {
                baos.writeTo(new FileOutputStream(localPath));
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片尺寸压缩
     * @param path   图片路径
     * @param width  指定宽度
     * @param height 指定高度
     * @return Bitmap
     */
    public static Bitmap compressBitmapSizeFromFile(String path, int width, int height) {
        BitmapFactory.Options opts = null;
        if (path != null) {
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
            }
            return BitmapFactory.decodeFile(path, opts);
        } else return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * bitmap尺寸简单压缩
     * @param bitmap 目标对象
     * @param width  指定宽度
     * @param height 指定高度
     * @return bitmap 结果对象
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
     * 获取圆角图片
     *
     * @param bitmap  目标对象
     * @param pixels  圆角大小
     * @return   结果对象
     */
    public static Bitmap toRoundCornerBitmap(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 保存图片到sdCard
     * @param newBitmap 要保存的bitmap
     * @param path      保存路径
     * @param photoName 保存后的名称
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
     * 获取所有图片的总大小
     * @param pathList 图片路径的集合
     * @return
     */
    public static String getPictureSize(List<String> pathList) {
        long totalSize = 0;
        for (String path : pathList) {
            File file = new File(path);
            if (file.exists() && file.isFile())
                totalSize += file.length();
        }
        NumberFormat ddf1 = NumberFormat.getNumberInstance();
        //保留小数点后两位
        ddf1.setMaximumFractionDigits(2);
        double size = totalSize / 1048576.0;
        return ddf1.format(size) + "M";
    }


}
