package cj.library.base;

import java.util.List;


import android.content.Context;
import android.os.Bundle;
import android.widget.BaseAdapter;

/**
 * @Description:自定义的BaseAdapter，所有adapter的父类。
 * Created by cj on 2015/11/22.
 */ 
public abstract class AppBaseAdapter<D> extends BaseAdapter {

	public Context context;
	public List<D> list;//


	public AppBaseAdapter(Context context, List<D> list) {
		this.context = context;
		this.list = list;
	}

	/**
	 * update
	 * @param list
	 */
	public void updateListView(List<D> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	


}
