package cj.library.view.comm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决ScrollView中嵌套ListView，listview显示不全
 * @Description:自定义无滚动条的ListView
 */
public class NoScrollListView extends ListView {
 	       
	    public NoScrollListView(Context context)
	    {   
	        super(context);    
	    }   
	       
	    public NoScrollListView(Context context, AttributeSet attrs)
	    {   
	        super(context, attrs);   
	    }   
	       
	    public NoScrollListView(Context context, AttributeSet attrs, int defStyle)
	    {   
	        super(context, attrs, defStyle);     
	    }   
	    @Override
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

	        int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    }     
}
