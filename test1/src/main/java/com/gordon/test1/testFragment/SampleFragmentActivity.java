package com.gordon.test1.testFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import com.gordon.test1.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public abstract class SampleFragmentActivity extends AppCompatActivity {

    @Bind(R.id.fl_singleFragment)
    FrameLayout flSingleFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sample);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        // 由于添加fragment但是出现意外情况时，如屏幕旋转，activity会重建，
        // 此时fragment也会重建，就会造成有两个fragment
        // 如果是只添加一个fragment：
        // savedInstanceState：在这里可以用来判断是否有fragment实例(fragment跟着activity销毁时会自动保存一些数据)
        if (savedInstanceState == null) {
            //fragment = new SingleFragment();
            // 写一个通用方法创建fragment
            fragment = createFragment();

            // 如果不考虑activity从意外到恢复时fragment的状态，可以用 commitAllowingStateLoss()提交
            //fragmentTransaction.add(R.id.fl_singleFragment,fragment).commit();
        }

        // 第二种方式：如果一个activity中想添加多个fragment
        //   String tag = OneFragment.class.getSimpleName();
        //   fragment = fragmentManager.findFragmentByTag(tag);
        //   if(fragment == null){
        //       fragment = new OneFragment();
        //   }
        //   tag = TwoFragment.class.getSimpleName();
        //   fragment = fragmentManager.findFragmentByTag(tag);
        //   if(fragment == null){
        //       fragment = new TwoFragment();
        //   }
        //   // replace()方法是先remove()再add()
        //   //fragmentTransaction.replace(R.id.fl_singleFragment,fragment).commit();
        //   // 最后是ImageLoader Demo中的使用方式：
    }

    protected abstract Fragment createFragment();

// ---从MainActivity点击跳转到SimpleImageActivity，SimpleImageActivity中添加了多个Fragment

//    public void onImageListClick(View view) {
//        Intent intent = new Intent(this, SimpleImageActivity.class);
//        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageListFragment.INDEX);
//        startActivity(intent);
//    }
//
//    public void onImageGridClick(View view) {
//        Intent intent = new Intent(this, SimpleImageActivity.class);
//        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridFragment.INDEX);
//        startActivity(intent);
//    }
//
//    public void onImagePagerClick(View view) {
//        Intent intent = new Intent(this, SimpleImageActivity.class);
//        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
//        startActivity(intent);
//    }
//
//    public void onImageGalleryClick(View view) {
//        Intent intent = new Intent(this, SimpleImageActivity.class);
//        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGalleryFragment.INDEX);
//        startActivity(intent);
//    }

//---- SimpleImageActivity

//   int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
//    Fragment fr;
//    String tag;
//    int titleRes;
//    switch (frIndex) {
//        default:
//        case ImageListFragment.INDEX:
//            tag = ImageListFragment.class.getSimpleName();
//            fr = getSupportFragmentManager().findFragmentByTag(tag);
//            if (fr == null) {
//                fr = new ImageListFragment();
//            }
//            titleRes = R.string.ac_name_image_list;
//            break;
//        case ImageGridFragment.INDEX:
//            tag = ImageGridFragment.class.getSimpleName();
//            fr = getSupportFragmentManager().findFragmentByTag(tag);
//            if (fr == null) {
//                fr = new ImageGridFragment();
//            }
//            titleRes = R.string.ac_name_image_grid;
//            break;
//        case ImagePagerFragment.INDEX:
//            tag = ImagePagerFragment.class.getSimpleName();
//            fr = getSupportFragmentManager().findFragmentByTag(tag);
//            if (fr == null) {
//                fr = new ImagePagerFragment();
//                fr.setArguments(getIntent().getExtras());
//            }
//            titleRes = R.string.ac_name_image_pager;
//            break;
//        case ImageGalleryFragment.INDEX:
//            tag = ImageGalleryFragment.class.getSimpleName();
//            fr = getSupportFragmentManager().findFragmentByTag(tag);
//            if (fr == null) {
//                fr = new ImageGalleryFragment();
//            }
//            titleRes = R.string.ac_name_image_gallery;
//            break;
//    }
//
//    setTitle(titleRes);
//    getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();


}
