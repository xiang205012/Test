package com.test.mytest.testA_Z_ListView;

import java.util.Comparator;

/**
 * 比较器
 * Created by Administrator on 2015/11/17.
 */
public class PinyinComparator implements Comparator<SortModel> {

    @Override
    public int compare(SortModel o1, SortModel o2) {
        if(o1.getSortLetter().equals("@")
                || o2.getSortLetter().equals("#")){
            return -1;
        }else if(o1.getSortLetter().equals("#")
                || o2.getSortLetter().equals("@")){
            return 1;
        }else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }
}
