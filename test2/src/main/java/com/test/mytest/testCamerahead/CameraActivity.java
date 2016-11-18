package com.test.mytest.testCamerahead;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.test.mytest.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 选择头像
 * Created by Administrator on 2015/8/13.
 */
public class CameraActivity extends Activity implements View.OnClickListener {

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;

    private Button button;
    private Button tailor;
    private ImageView imageView;
    private static final int SCALE = 5;//压缩比例
    private String TAG = "CCC=====>>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        button = (Button) findViewById(R.id.button2);
        tailor = (Button) findViewById(R.id.button3);
        imageView = (ImageView) findViewById(R.id.imageView2);

        button.setOnClickListener(this);
        tailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //截图后显示
                showPicturePicker(CameraActivity.this,true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择头像");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                    case TAKE_PICTURE://拍照
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.png"));
                        Log.i(TAG, "imageUri的路径：" + Environment.getExternalStorageDirectory());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//MediaStore.EXTRA_OUTPUT关联到一个Uri，此Uri是用来存放Bitmap的
                        startActivityForResult(intent, TAKE_PICTURE);
                        /**第二种方式：
                         * 附加选项    数据类型    描述
                         crop    String  发送裁剪信号
                         aspectX int X方向上的比例
                         aspectY int Y方向上的比例
                         outputX int 裁剪区的宽
                         outputY int 裁剪区的高
                         scale   boolean 是否保留比例
                         return-data boolean 是否将数据保留在Bitmap中返回
                         data    Parcelable  相应的Bitmap数据
                         circleCrop  String  圆形裁剪区域？
                         MediaStore.EXTRA_OUTPUT ("output")  URI 将URI指向相应的file:///...
                         如果你将return-data设置为“true”，你将会获得一个与内部数据关联的Action，
                         并且bitmap以此方式返回：(Bitmap)extras.getParcelable("data")。
                         注意：如果你最终要获取的图片非常大，那么此方法会给你带来麻烦，所以你要控制outputX和outputY保持在较小的尺寸。
                         intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                         intent.setData(imgUri);
                         intent.putExtra("outputX", 300);
                         intent.putExtra("outputY", 300);
                         intent.putExtra("aspectX", 1);
                         intent.putExtra("aspectY", 1);
                         intent.putExtra("scale", true);
                         intent.putExtra("return-data", true);
                         intent.putExtra(MediaStore.EXTRA_OUTPUT);
                         startActivityForResult(intent, 0);
                         */
                        break;
                    case CHOOSE_PICTURE://相册中选
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, CHOOSE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "resultCode：" + resultCode);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.png");
                    //第二种方式：
                    //Bundle bundle = data.getExtras();
                    //Bitmap bitmap = bundle.get("data");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,bitmap.getWidth()/SCALE,bitmap.getHeight()/SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    imageView.setImageBitmap(newBitmap);
                    //保存到SDCard:ImageTools.savePhotoToSDCard(newBitmap,Environment.getExternalStorageDirectory().getAbsolutePath(),"拍照"+String.valueOf(System.currentTimeMillis()));
                    //保存到内存：
                    String path = this.getCacheDir()+"chen";
                    ImageTools.savePhotoToSDCard(newBitmap, path, "拍照" + String.valueOf(System.currentTimeMillis()));
                    //在系统相册中显示：前提也是要现在保存，然后把uri发广播告诉系统
//                    MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"","");
//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri uri = Uri.fromFile(new File("/sdcard/image.png"));
//                    intent.setData(uri);
//                    this.sendBroadcast(intent);
                    break;
                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    if(originalUri == null){
                        Log.i(TAG, "originalUri：" + originalUri);
                    }
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,originalUri);
                        if(photo != null){
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo,photo.getWidth()/SCALE,photo.getHeight()/SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            imageView.setImageBitmap(smallBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        System.out.println("Data");
                    }else {
                        System.out.println("File");
                        String fileName = getSharedPreferences("temp",Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
                    }
                    cropImage(uri, 500, 500, CROP_PICTURE);//调用系统裁剪功能
                    break;

                case CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap)extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        }
                    }
                    imageView.setImageBitmap(photo);
                    break;
            }
        }
    }


    /**
     * 截图后显示
     * @param
     * @param
     */
    private void showPicturePicker(Context context, boolean isCrop) {
        final boolean crop = isCrop;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            //类型码：
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                    case TAKE_PICTURE:
                        Uri imageUri = null;
                        String fileName = null;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (crop) {
                            REQUEST_CODE = CROP;
                            //删除上一次截图的临时文件
                            SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
                            ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
                            //保存本次截图是文件名字
                            fileName = String.valueOf(System.currentTimeMillis()) + ".png";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("tempName", fileName);
                            editor.commit();
                        } else {
                            REQUEST_CODE = TAKE_PICTURE;
                            fileName = "image.png";
                        }
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                        //指定照片保存路径（sd卡），image.png为一个临时文件，每次拍照后这个图片都会被替换
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case CHOOSE_PICTURE:
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (crop) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                }
            }
        });
        builder.show();
    }


    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

}
