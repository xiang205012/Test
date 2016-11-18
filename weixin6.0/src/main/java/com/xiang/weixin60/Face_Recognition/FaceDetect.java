package com.xiang.weixin60.Face_Recognition;

import android.graphics.Bitmap;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.xiang.weixin60.Constant;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by gordon on 2016/8/4.
 */
public class FaceDetect {

    public interface CallBack{
        void success(JSONObject jsonObject);
        void error(FaceppParseException exception);
    }

    /**
     * 检测人脸
     * @param bitmap    传入有人脸的图片
     * @param callBack  检测结果回调
     */
    public static void detect(final Bitmap bitmap,final CallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequests requests = new HttpRequests(Constant.FACE_KEY,Constant.FACE_RECRET,true,true);
                    // 创建一个附件，这样不会直接引用到传入的bitmap,以免出现销毁不的情况而导致内存溢出
                    Bitmap bmSmall = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight());
                    // 将图片压缩到 ByteArrayOutputStream 中
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmSmall.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // 得到图片的二进制字节数组并作为参数进行检测
                    byte[] arrays = stream.toByteArray();
                    // 设置检测参数
                    PostParameters postParameters = new PostParameters();
                    postParameters.setImg(arrays);
                    // 检测并返回检测结果
                    JSONObject jsonObject = requests.detectionDetect(postParameters);

                    if(callBack != null){
                        callBack.success(jsonObject);
                    }

                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    if(callBack != null){
                        callBack.error(e);
                    }
                }


            }
        }).start();
    }

//    public static Bitmap createBitmap (Bitmap src)
//    从原位图src复制出一个新的位图，和原始位图相同
//
//    public static Bitmap createBitmap (int[] colors, int width, int height, Bitmap.Config config)
//    这个函数根据颜色数组来创建位图，注意:颜色数组的长度>=width*height
//
//    此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。config是一个枚举，可以用它来指定位图“质量”。
//
//    public static Bitmap createBitmap (int[] colors, int offset, int stride, int width, int height, Bitmap.Config config)
//    此方法与2类似，但我还不明白offset和stride的作用。
//
//    public static Bitmap createBitmap (Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
//    从原始位图剪切图像，这是一种高级的方式。可以用Matrix(矩阵)来实现旋转等高级方式截图
//    参数说明：
//            　　Bitmap source：要从中截图的原始位图
//    　　int x:起始x坐标
//    　　int y：起始y坐标
//    int width：要截的图的宽度
//    int height：要截的图的宽度
//    Bitmap.Config  config：一个枚举类型的配置，可以定义截到的新位图的质量
//    返回值：返回一个剪切好的Bitmap

//    public static Bitmap createBitmap (int width, int height, Bitmap.Config config)
//    根据参数创建新位图
//
//    public static Bitmap createBitmap (Bitmap source, int x, int y, int width, int height)
//    简单的剪切图像的方法，可以参考上面的4.
}
