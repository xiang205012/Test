package com.gordon.test1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/2/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**Activity之间跳转的方式*/
    public enum TransitionMode{
        /**当前activity从左边进入，上一个activity往右边退出*/
        LEFT,
        /**当前activity从右边进入，上一个activity往左边退出*/
        RIGHT,
        /**当前activity从上进入，上一个activity往下退出*/
        TOP,
        /**当前activity从下进入，上一个activity往上退出*/
        BOTTOM,
        /**当前activity放大进入，上一个activity缩小退出*/
        SCALE,
        /**当前activity 透明度渐渐变小进入，上一个activity透明度渐渐变大退出*/
        FADE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(toggleOverridePendingTrasition()){
            switch(getOverridePendingTrasitionMode()){
                case LEFT:
                    // 当前activity执行left_in  上一个activity执行right_out
                    overridePendingTransition(R.anim.left_in,R.anim.right_out);
                    break;
                case RIGHT:
                    // 当前activity执行left_in  上一个activity执行right_out
                    overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in,R.anim.bottom_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,R.anim.top_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    break;
            }
        }

        super.onCreate(savedInstanceState);
    }

    protected abstract boolean toggleOverridePendingTrasition();

    protected abstract TransitionMode getOverridePendingTrasitionMode();


}
