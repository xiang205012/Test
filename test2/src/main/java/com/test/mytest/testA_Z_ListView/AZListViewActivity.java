package com.test.mytest.testA_Z_ListView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class AZListViewActivity extends Activity implements A_Z_View.LetterChangeListener {

    private ListView listView;
    private A_Z_View sideBar;
    /**
     * 带删除按钮的editText
     */
    private ClearEditText clearEditText;
    private AZListAdapter adapter;
    private List<SortModel> list;
    /**
     * 汉字转成拼音工具类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面数据的工具类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.az_listview_activity);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        listView = (ListView) findViewById(R.id.azlistView);
        sideBar = (A_Z_View) findViewById(R.id.sideBar);
        clearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //设置右侧触摸监听
        sideBar.setLetterChangeListener(this);

        list = filledData(getResources().getStringArray(R.array.date));

        //将list按A-Z排序
        Collections.sort(list,pinyinComparator);
        adapter = new AZListAdapter(this,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Log.i("TAG", ((SortModel) adapter.getItem(position)).getName());
            }
        });
        /**
         * 设置搜索监听
         */
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = list;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : list){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    /**
     * 获取填充listView的数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> list = new ArrayList<SortModel>();
        for(int i = 0;i < date.length;i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String shouZiMu = pinyin.substring(0,1).toUpperCase();
            if(shouZiMu.matches("[A-Z]")){
                sortModel.setSortLetter(shouZiMu);
            }else{
                sortModel.setSortLetter("#");
            }
            list.add(sortModel);
        }

        return list;
    }

    @Override
    public void letterChange(String text) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(text.charAt(0));
        if(position != -1){
            listView.setSelection(position);
        }
    }
}
