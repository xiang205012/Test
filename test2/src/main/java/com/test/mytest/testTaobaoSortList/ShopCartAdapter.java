package com.test.mytest.testTaobaoSortList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.util.ArrayMap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class ShopCartAdapter extends BaseExpandableListAdapter {

    private List<StoreInfo> groups;
    private ArrayMap<String,List<GoodsInfo>> children;
    private Context context;
    private LayoutInflater inflater;

    public ShopCartAdapter(List<StoreInfo> groups,
                           ArrayMap<String,List<GoodsInfo>> children,Context context){
        this.groups = groups;
        this.children = children;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder gHolder;
        if(convertView == null){
            gHolder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_group,null);
            gHolder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
            gHolder.store_edtor = (Button) convertView.findViewById(R.id.tv_store_edtor);
            gHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            convertView.setTag(gHolder);
        }else{
            gHolder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        gHolder.tv_group_name.setText(group.getName());
        gHolder.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setChoosed(((CheckBox) v).isChecked());
                if (checkListener != null) {
                    checkListener.checkGroup(groupPosition, ((CheckBox) v).isChecked());
                    notifyDataSetChanged();
                }
            }
        });
        // 此句主要用于 adapter刷新时更新group checkBox的状态
        gHolder.cb_check.setChecked(group.isChoosed());
        // 此句主要用于 adapter刷新时更新group 的编辑状态
        if(group.isEdtor()){
            gHolder.store_edtor.setText("完成");
        }else{
            gHolder.store_edtor.setText("编辑");
        }
        gHolder.store_edtor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group.isEdtor()){
                    group.setIsEdtor(false);
                }else{
                    group.setIsEdtor(true);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder cholder;
        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.item_shopcart_product,parent,false);
//            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
            cholder.cb_check = (CheckBox) convertView.findViewById(R.id.cb_item);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_item_name);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_item_price);
            cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_item_add);
//            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_item_sub);
            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_item_sub);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_item_count2);
            cholder.rl_no_edtor = (RelativeLayout) convertView.findViewById(R.id.rl_item_desc);
            cholder.tv_color_size = (TextView) convertView.findViewById(R.id.tv_item_desc);
            cholder.tv_discount_price = (TextView) convertView.findViewById(R.id.tv_discount_price);
            cholder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_item_count);
            cholder.ll_edtor = (RelativeLayout) convertView.findViewById(R.id.rl_item_delete);
            cholder.tv_colorsize = (TextView) convertView.findViewById(R.id.tv_delete_color_size);
            cholder.tv_goods_delete = (TextView) convertView.findViewById(R.id.tv_item_delete2);
            cholder.iv_adapter_list_pic= (ImageView) convertView.findViewById(R.id.iv_item);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }
        if(groups.get(groupPosition).isEdtor()){
            cholder.ll_edtor.setVisibility(View.VISIBLE);
            cholder.rl_no_edtor.setVisibility(View.GONE);
        }else{
            cholder.ll_edtor.setVisibility(View.GONE);
            cholder.rl_no_edtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(groupPosition,childPosition);
        if(goodsInfo != null){
            cholder.tv_product_desc.setText(goodsInfo.getDesc());
            cholder.tv_price.setText("￥" + goodsInfo.getPrice() + "");
            cholder.tv_count.setText(goodsInfo.getCount() + "");
            cholder.iv_adapter_list_pic.setImageResource(goodsInfo.getGoodsImg());
//            cholder.tv_color_size.setText("颜色：" + goodsInfo.getColor() + "," + "尺码：" + goodsInfo.getSize() + "瓶/斤");
            /**Spanned.SPAN_EXCLUSIVE_EXCLUSIVE，这是在 setSpan 时需要指定的 flag，
             * 它是用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果。
             * 分别有 Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)、
             * Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)、
             * Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)、
             * Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
             * SpannableString属性详解看该类最后*/
            SpannableString spanString = new SpannableString("￥"+String.valueOf(goodsInfo.getDiscountPrice()));
            StrikethroughSpan span = new StrikethroughSpan();
            spanString.setSpan(span,0,String.valueOf(goodsInfo.getDiscountPrice()).length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 避免无限次append
            if(cholder.tv_discount_price.getText().toString().length()>0){
                cholder.tv_discount_price.setText("");
            }
            cholder.tv_discount_price.append(spanString);
            cholder.tv_buy_num.setText("x" + goodsInfo.getCount());
            cholder.cb_check.setChecked(goodsInfo.isChoosed());
            cholder.cb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    if (checkListener != null) {
                        checkListener.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                    }
                }
            });
            // 商品数量加
            cholder.iv_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkListener != null){
                        checkListener.countAdd(groupPosition,childPosition,
                                cholder.tv_count,cholder.cb_check.isChecked());
                    }
                }
            });
            // 商品数量减
            cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkListener != null){
                        checkListener.countSub(groupPosition,childPosition,
                                cholder.tv_count,cholder.cb_check.isChecked());
                    }
                }
            });
            // 删除该商品
            cholder.tv_goods_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("操作提示");
                    alertDialog.setMessage("您确定要删除该商品");
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(checkListener != null){
                                checkListener.childDelete(groupPosition,childPosition);
                            }
                        }
                    });
                    alertDialog.show();
                }
            });

        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组元素绑定器
     */
    private class GroupViewHolder {
        CheckBox cb_check;
        TextView tv_group_name;
        Button store_edtor;
    }

    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        CheckBox cb_check;
        ImageView iv_adapter_list_pic;
        TextView tv_product_name;
        TextView tv_product_desc;
        TextView tv_price;
        TextView iv_increase;
        TextView tv_count;
        TextView iv_decrease;
        RelativeLayout rl_no_edtor;
        TextView tv_color_size;
        TextView tv_discount_price;
        TextView tv_buy_num;
        RelativeLayout ll_edtor;
        TextView tv_colorsize;
        TextView tv_goods_delete;
    }
    private CheckListener checkListener;
    public void setCheckListener(CheckListener checkListener){
        this.checkListener = checkListener;
    }
    public interface CheckListener{
        /**
         *组选框状态改变触发的事件
         * @param groupPosition 组元素位置
         * @param isCheckGroup  组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isCheckGroup);

        /**
         *子选框状态改变时触发的事件
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isCheckChild  子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isCheckChild);

        /**
         * 增加操作
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void countAdd(int groupPosition, int childPosition, TextView showCountView, boolean isChecked);
        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void countSub(int groupPosition, int childPosition, TextView showCountView, boolean isChecked);
        /**
         * 删除子item
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);

    }

}
/**SpannableString属性详解
 *   1、BackgroundColorSpan 背景色
     2、ClickableSpan 文本可点击，有点击事件
     3、ForegroundColorSpan 文本颜色（前景色）
     4、MaskFilterSpan 修饰效果，如模糊(BlurMaskFilter)、浮雕(EmbossMaskFilter)
     5、MetricAffectingSpan 父类，一般不用
     6、RasterizerSpan 光栅效果
     7、StrikethroughSpan 删除线（中划线）
     8、SuggestionSpan 相当于占位符
     9、UnderlineSpan 下划线
     10、AbsoluteSizeSpan 绝对大小（文本字体）
     11、DynamicDrawableSpan 设置图片，基于文本基线或底部对齐。
     12、ImageSpan 图片
     13、RelativeSizeSpan 相对大小（文本字体）
     14、ReplacementSpan 父类，一般不用
     15、ScaleXSpan 基于x轴缩放
     16、StyleSpan 字体样式：粗体、斜体等
     17、SubscriptSpan 下标（数学公式会用到）
     18、SuperscriptSpan 上标（数学公式会用到）
     19、TextAppearanceSpan 文本外貌（包括字体、大小、样式和颜色）
     20、TypefaceSpan 文本字体
     21、URLSpan 文本超链接*/