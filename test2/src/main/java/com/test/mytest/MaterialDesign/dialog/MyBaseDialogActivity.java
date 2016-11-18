package com.test.mytest.MaterialDesign.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.mytest.MaterialDesign.cardview.MyCardViewActivity;
import com.test.mytest.MaterialDesign.toolbar.MyToolBarActivity;
import com.test.mytest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cj.library.base.AppBaseActivity;
import cj.library.view.comm.CustomDialog;

//import com.afollestad.materialdialogs.AlertDialogWrapper;
//import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Administrator on 2015/11/25.
 */
public class MyBaseDialogActivity extends AppBaseActivity {

    @InjectView(R.id.base_dialog)
    Button base_dialog;
    @InjectView(R.id.alert_dialog)
    Button alert_dialog;
    @InjectView(R.id.list_dialog)
    Button list_dialog;
    @InjectView(R.id.bt_cardview)
    Button bt_cardview;
    @InjectView(R.id.bt_toolbar)
    Button bt_toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materia_dialog_activity);
        ButterKnife.inject(this);


    }

    @Override
    protected boolean toggleOverridePendingTrasition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTrasitionMode() {
        return null;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @OnClick({R.id.base_dialog,R.id.alert_dialog,R.id.list_dialog,
            R.id.bt_cardview,R.id.bt_toolbar})
    public void click(View v){
        Intent intent = null;
        switch(v.getId()){
            case R.id.base_dialog:
//                //目前还没找到监听的方法，只认为是简单的展示内容
//                MaterialDialog dialog = new MaterialDialog.Builder(this)
//                            .title("你好MaterialDialog")
//                            .content("第一次接触MaterialDialog")
//                            .positiveText("确定")
//                            .negativeText("取消")
//                            .iconRes(R.drawable.help_ico)
//                            .show();

                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setTitle(R.string.prompt);
                builder.setMessage(R.string.exit_app);
                builder.setPositiveButton("确定", null);
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.alert_dialog:
              //  AlertDialogWrapper.Builder alertDialog = new AlertDialogWrapper.Builder(this);
              //  alertDialog.setTitle("警告dialog")
              //          .setMessage("nijlsfjlsjfs")
              //          .setNegativeButton("ok", new DialogInterface.OnClickListener() {
              //              @Override
              //              public void onClick(DialogInterface dialog, int which) {
//
              //              }
              //          })
              //          .setNeutralButton("neutral", new DialogInterface.OnClickListener() {
              //              @Override
              //              public void onClick(DialogInterface dialog, int which) {
//
              //              }
              //          })
              //          .setPositiveButton("no", new DialogInterface.OnClickListener() {
              //              @Override
              //              public void onClick(DialogInterface dialog, int which) {
//
              //              }
              //          });
              //  alertDialog.show();
                break;
            case R.id.list_dialog:
             //   MaterialDialog listDialog = new MaterialDialog.Builder(this)
             //           .title("list chose")
             //           .items(R.array.preference_values)
             //           .itemsCallback(new MaterialDialog.ListCallback() {
             //               @Override
             //               public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
             //                   LogUtils.d("选择了第" + i +"个");
             //               }
             //           }).show();//show()方法包含了build()，如果没有show()就必须要build()来构建
             //           //.build();
                break;
            case R.id.bt_toolbar:
                intent = new Intent(this, MyToolBarActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_cardview:
                intent = new Intent(this, MyCardViewActivity.class);
                startActivity(intent);
                break;

        }
    }


}
