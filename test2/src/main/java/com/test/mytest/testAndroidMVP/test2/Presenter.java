package com.test.mytest.testAndroidMVP.test2;

/**
 * Created by Administrator on 2016/1/19.
 */
public class Presenter extends BasePresenter{

    private ILoadView iLoadView;
    private WeaterModelPresenter weaterModelPresenter;


    public Presenter(ILoadView iLoadView){
        this.iLoadView = iLoadView;
        weaterModelPresenter = new WeaterModelPresenter();
    }

    public void getWeater(){
//        weaterModelPresenter.loadWeater("biejing",this);
        weaterModelPresenter.loadWeater("biejing");
    }

    public void showDialog(){
        iLoadView.showDialog();
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if(object != null)
        iLoadView.setModel((WeaterModel) object);
    }

    @Override
    public void onError() {
        super.onError();
        iLoadView.showError();
    }

    //    @Override
//    public void onSuccess(Object object) {
//        if(object != null)
//        iLoadView.setModel((WeaterModel) object);
//    }
//
//    @Override
//    public void onError() {
//        iLoadView.showError();
//    }
}
