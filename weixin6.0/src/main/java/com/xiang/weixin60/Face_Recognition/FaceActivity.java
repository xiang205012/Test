package com.xiang.weixin60.Face_Recognition;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.xiang.weixin60.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gordon on 2016/8/4.
 */
public class FaceActivity extends Activity {


    @InjectView(R.id.getImage)
    Button getImage;
    @InjectView(R.id.btn_detect)
    Button btnDetect;
    @InjectView(R.id.tv_showMsg)
    TextView tvShowMsg;
    @InjectView(R.id.face_iv)
    ImageView faceIv;
    private int PICK_CODE = 0X11;
    private String currentPhotoPath;// 获取的图片路径
    private Bitmap photoImg;

    private static final int MSG_SUCCESS = 0X123;
    private static final int MSG_ERROR = 0X1234;

    private Paint mPaint;

    /**在图片上显示性别图标和年龄*/
    private TextView imgTextView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SUCCESS:
                    JSONObject result = (JSONObject) msg.obj;
                    prepareRsBitmap(result);// 根据结果绘制人脸信息并附在原来的图片上
                    faceIv.setImageBitmap(photoImg);
                    break;
                case MSG_ERROR:
                    String errorMsg = (String) msg.obj;
                    if(TextUtils.isEmpty(errorMsg)){
                        Log.d("tag-->>","检测出错了...");
                    }else{
                        Log.d("tag-->>",errorMsg);
                    }
                    break;
            }
        }
    };

    private void prepareRsBitmap(JSONObject result) {
        // 如果图片是经过二次取样的，返回的result会是null
        // 例如拿手机从电脑上拍一张人脸图片进行检测，很有可能返回null，或者检测过程漫长
        if(result == null){
            Toast.makeText(this,"请使用原始的图片",Toast.LENGTH_LONG).show();
            return;
        }

        // 创建一张空的和photoImg大小一样的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(photoImg.getWidth(),photoImg.getHeight(),photoImg.getConfig());
        // 创建一张有一个bitmap的canvas，此时所有在此canvas上绘制的东西其实都是在newBitmap上绘制
        Canvas canvas = new Canvas(newBitmap);
        // 将原图绘制到canvas上 此时bitmap就有一张图片了
        canvas.drawBitmap(photoImg,0,0,null);

        try {
            JSONArray faceList = result.getJSONArray("face");
            int faceCount = faceList.length();
            tvShowMsg.setText(faceCount + " 张脸");
            for(int i = 0; i < faceCount; i++){
                JSONObject face = faceList.getJSONObject(i);
                JSONObject position = face.getJSONObject("position");

                float w = (float) position.getDouble("width");
                float h = (float) position.getDouble("height");

                float x = (float) position.getJSONObject("center").getDouble("x");
                float y = (float) position.getJSONObject("center").getDouble("y");

                // 由于都是百分比值，先转换为真实的值
                w = w / 100 * newBitmap.getWidth();
                h = h / 100 * newBitmap.getHeight();
                // 中心点的坐标
                x = x / 100 * newBitmap.getWidth();
                y = y / 100 * newBitmap.getHeight();

                // 绘制检测到的人脸矩形
                mPaint.setColor(0xffffffff);
                //canvas.drawLine(x - w/2, y - h/2, x + w/2, y - h/2,mPaint);
                //canvas.drawLine(x + w/2, y - h/2, x + w/2, y + h/2,mPaint);
                //canvas.drawLine(x + w/2, y + h/2, x - w/2, y + h/2,mPaint);
                //canvas.drawLine(x - w/2, y + h/2, x - w/2, y - h/2,mPaint);
                // 由于绘制直线时没有考虑到线宽，所以结果是折角处有空隙，如果要修复，在绘制时需加上线宽再计算位置
                // 这里可直接绘制矩形解决这个问题
                RectF rect = new RectF();
                rect.left = (int) (x - w/2);
                rect.top = (int) (y - h/2);
                rect.right = (int) (x + w/2);
                rect.bottom = (int) (y + h/2);
                mPaint.setStyle(Paint.Style.STROKE);// 画空心的矩形
                canvas.drawRoundRect(rect,5,5,mPaint);
                // 绘制完成后，newBitmap上就有photoImg这张图片和一个矩形框

                // 最后绘制性别和年龄 将textView转为bitmap然后添加到newBitmap上
                JSONObject attribute = face.getJSONObject("attribute");
                JSONObject age = attribute.getJSONObject("age");
                JSONObject gender = attribute.getJSONObject("gender");

                int curAge = age.getInt("value");
                String curGender = gender.getString("value");

                Bitmap ageBitmap = buildAgeBitmap(curAge,curGender.equals("Male"));

                // 由于bitmap的显示大小会随着ImageView的设置而缩放，因此需要考虑textView的大小
                // (一定要参照原图大小来设定textView的大小，才不会出现显示异常)
                int ageWidth = ageBitmap.getWidth();
                int ageHeight = ageBitmap.getHeight();
                if(newBitmap.getWidth() < photoImg.getWidth()
                        && newBitmap.getHeight() < photoImg.getHeight()){
                    float radio = Math.max(newBitmap.getWidth() * 1.0f / photoImg.getWidth() * 1.0f ,
                                            newBitmap.getHeight() * 1.0f / photoImg.getHeight() * 1.0f);
                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap,(int)(ageWidth * radio),(int)(ageHeight * radio),false);
                }

                canvas.drawBitmap(ageBitmap,x - ageBitmap.getWidth()/2,y - h/2 - ageBitmap.getHeight() - mPaint.getStrokeWidth(),null);
                // 绘制完成后newBitmap上就有 原图，矩形框，textView显示的框

                photoImg = newBitmap;// 重新赋值给photoImg,并显示
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap buildAgeBitmap(int curAge, boolean isMale) {
        imgTextView.setText(curAge+"");
        if(isMale){
            imgTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male),null,null,null);
        }else {
            imgTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female),null,null,null);
        }

        // 一定要写下面两句，view要转换成bitmap一定要经过measure() layout()，否则getDrawingCache()常常返回null
        imgTextView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imgTextView.layout(0, 0, imgTextView.getMeasuredWidth(), imgTextView.getMeasuredHeight());

        // textView转换为bitmap
        imgTextView.setDrawingCacheEnabled(true);
        //imgTextView.buildDrawingCache();此句跟上一句功能一样
        Bitmap bitmap = imgTextView.getDrawingCache();
        //imgTextView.destroyDrawingCache();不要写此句，否则会出现 trying user recycerd bitmap,意思是用一张已经消耗的bitmap了

        return bitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        ButterKnife.inject(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);

        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgTextView = new TextView(this);
        imgTextView.setGravity(Gravity.CENTER);
        imgTextView.setTextSize(26);
        imgTextView.setTextColor(getResources().getColor(R.color.white));
        imgTextView.setCompoundDrawablePadding(5);
        imgTextView.setBackgroundColor(getResources().getColor(R.color.line_color));
        imgTextView.setLayoutParams(tvParams);
    }


    @OnClick({R.id.btn_detect,R.id.getImage})
    public void click(View view){
        switch(view.getId()){
            case R.id.getImage:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_CODE);
                break;
            case R.id.btn_detect:
                if(TextUtils.isEmpty(currentPhotoPath)){
                    photoImg = BitmapFactory.decodeResource(getResources(),R.drawable.t4);
                }else {
                    resizePhoto();
                }
                FaceDetect.detect(photoImg, new FaceDetect.CallBack() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        Message message = handler.obtainMessage();
                        message.what = MSG_SUCCESS;
                        message.obj = jsonObject;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void error(FaceppParseException exception) {
                        Message message = handler.obtainMessage();
                        message.what = MSG_SUCCESS;
                        message.obj = exception.getErrorMessage();
                        handler.sendMessage(message);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == PICK_CODE){
            if(data != null){
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                currentPhotoPath = cursor.getString(idx);
                cursor.close();

                resizePhoto();// 压缩图片
                faceIv.setImageBitmap(photoImg);

            }
        }

    }

    private void resizePhoto() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath,options);
        double radio = Math.max(options.outWidth*1.0d/1024f,options.outHeight*1.0d/1024f);
        options.inSampleSize = (int) Math.ceil(radio);
        options.inJustDecodeBounds = false;
        photoImg = BitmapFactory.decodeFile(currentPhotoPath, options);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPaint = null;
        handler = null;
        photoImg = null;
        imgTextView = null;
    }
}
