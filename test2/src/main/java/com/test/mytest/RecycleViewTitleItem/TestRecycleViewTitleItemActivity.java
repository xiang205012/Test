package com.test.mytest.RecycleViewTitleItem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;


import com.github.promeg.pinyinhelper.Pinyin;
import com.test.mytest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gordon on 2016/11/3.
 */

public class TestRecycleViewTitleItemActivity extends Activity{

    RecyclerView recycleView;
    private List<CityBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recycleView = new RecyclerView(this);
        setContentView(recycleView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(manager);

        initDatas(getResources().getStringArray(R.array.date));

        CityAdapter adapter = new CityAdapter(mDatas);
        recycleView.setAdapter(adapter);

        recycleView.addItemDecoration(new TitleItemDecoration(TestRecycleViewTitleItemActivity.this,mDatas));
        recycleView.addItemDecoration(new DividerItemDecoration(TestRecycleViewTitleItemActivity.this,DividerItemDecoration.VERTICAL_LIST));

        sortData();
    }

    private void sortData() {
        List<CityBean> lists = mDatas;
        for (int i = 0; i < lists.size(); i++) {
            CityBean bean = lists.get(i);
            StringBuilder stringBuilder = new StringBuilder();
            String cityName = bean.getCityName();
            for (int j = 0 ; j < cityName.length() ; j++){
                stringBuilder.append(Pinyin.toPinyin(cityName.charAt(j)).toUpperCase());
            }
            bean.setCityNamePinyin(stringBuilder.toString());

            String firstLetter = stringBuilder.toString().substring(0,1);
            if (firstLetter.matches("[A-Z]")){
                bean.setCityNamePinyinFirstLetter(firstLetter);
            }else {
                bean.setCityNamePinyinFirstLetter("#");
            }
        }
        Collections.sort(lists, new Comparator<CityBean>() {
            @Override
            public int compare(CityBean cityBean, CityBean t1) {
                if (cityBean.getCityNamePinyinFirstLetter().equals("#")){
                    return 1;
                } else if (t1.getCityNamePinyinFirstLetter().equals("#")){
                    return -1;
                } else {
                    return cityBean.getCityNamePinyinFirstLetter().compareTo(t1.getCityNamePinyinFirstLetter());
                }
            }
        });
    }

    private void initDatas(String[] stringArray) {
        for (int i = 0; i < stringArray.length; i++) {
            CityBean bean = new CityBean(stringArray[i]);
            mDatas.add(bean);
        }
    }

    private class CityAdapter extends RecyclerView.Adapter<ViewHolder>{

        private List<CityBean> datas;

        public CityAdapter(List<CityBean> lists){
            this.datas = lists;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1,-2);
            TextView textView = new TextView(parent.getContext());
            textView.setPadding(30,10,0,10);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
//            textView.setBackgroundColor(Color.parseColor("#FFDFDFDF"));
            textView.setTextColor(Color.parseColor("#FF999999"));
            textView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,parent.getContext().getResources().getDisplayMetrics()));
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(datas.get(position).getCityName());
        }
    }



    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
