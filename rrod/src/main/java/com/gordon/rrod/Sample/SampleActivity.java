package com.gordon.rrod.Sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gordon.rrod.R;
import com.gordon.rrod.Sample.api.RestApi;
import com.gordon.rrod.Sample.data.LoginInputEntity;
import com.gordon.rrod.Sample.http.HttpControl;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gordon on 2016/5/26.
 */
public class SampleActivity extends AppCompatActivity {

    //测试账号
    public static final String TEST_TENANT_NAME = "LingtuyangDemo";
    public static final String TEST_USER_NAME = "lty";
    public static final String TEST_PASSWORK = "20091123";

    private HttpControl httpControl;

    private RestApi restApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpControl = new HttpControl();
        restApi = httpControl.getRestApi();

        login();

    }

    private void login() {
        restApi.login(TEST_TENANT_NAME,TEST_USER_NAME,TEST_PASSWORK,null,null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoginSubscriber());

//        observable.subscribe(new LoginSubscriber());
    }


    private class LoginSubscriber extends Subscriber<String>{

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String s) {
            Log.i("tag","result : "+s);
        }
    }


//    //登陆
//    @Override
//    public Observable<Boolean> login(String tenantName, String userName, String password, String checkCode, String secretProtectCode) {
//
//        if (loginCache.isCached(userName)){
//            return login(tenantName, userName, password, checkCode, secretProtectCode);
//        }
//
//        final LoginInputEntity loginInputEntity = new LoginInputEntity(tenantName, userName, password);
//
//        return this.restApi.login(tenantName, userName, password, checkCode, secretProtectCode)
//                .map(new HttpResultFunc<LoginInfoEntity>())
//                .map(new Func1<HttpResultDtoEntity<LoginInfoEntity>, Boolean>() {
//                    @Override
//                    public Boolean call(HttpResultDtoEntity<LoginInfoEntity> loginInfoEntityHttpResultDtoEntity) {
//                        boolean isSuccess = loginInfoEntityHttpResultDtoEntity.isSuccess();
//
//                        if (isSuccess && loginCache != null) {
//                            //缓存登录数据
//                            loginCache.putLoginInput(loginInputEntity);
//                            loginCache.putTenantInfo(loginInfoEntityHttpResultDtoEntity.getData().getTenant());
//                            loginCache.putUserInfo(loginInfoEntityHttpResultDtoEntity.getData().getUser());
//                            loginCache.putPositionInfo(loginInfoEntityHttpResultDtoEntity.getData().getPositionInfo());
//                        }
//
//                        return isSuccess;
//                    }
//                })
//                .subscribeOn(DataConstant.DEBUG ? Schedulers.immediate() : Schedulers.io())
//                .unsubscribeOn(DataConstant.DEBUG ? Schedulers.immediate() : Schedulers.io())
//                .observeOn(DataConstant.DEBUG ? Schedulers.immediate() : AndroidSchedulers.mainThread())//AndroidSchedulers.mainThread()
//                ;
//    }
}
