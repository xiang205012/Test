package com.xiang.testapp.shoppingCart;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiang.testapp.R;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gordon on 2016/10/24.
 */

public class ShopCartAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<GroupInfo> groupInfos;
    private Map<String, List<GoodsInfo>> childrens;

    // group 监听接口
    private OnGroupClickListener mGroupListener;
    public void setOnGroupClickListener(OnGroupClickListener listener){
        this.mGroupListener = listener;
    }
    public interface OnGroupClickListener{
        void onClickGroupCheckBox(int position, boolean isGroupCheck);
        void onClickGroupEdit(int position, boolean isGroupEdit);
    }

    // child 监听接口
    private OnChildClickListener mChildListener;
    public void setOnChildClickListener(OnChildClickListener listener){
        this.mChildListener = listener;
    }
    public interface OnChildClickListener{
        void onClickLess(int groupPosition, int childPosition, TextView tvCount, boolean isChecked);
        void onClickAdd(int groupPosition, int childPosition, TextView tvCount, boolean isChecked);
        void onClickChildCheckBox(int groupPosition, int childPosition);
        void onClickChildDelete(int groupPosition, int childPosition);
    }

    public ShopCartAdapter(Context context, List<GroupInfo> groupInfos, Map<String, List<GoodsInfo>> childrens) {
        this.context = context;
        this.groupInfos = groupInfos;
        this.childrens = childrens;
    }

    @Override
    public int getGroupCount() {
        return groupInfos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groudId = groupInfos.get(groupPosition).id;
        return childrens.get(groudId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupInfos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String groudId = groupInfos.get(groupPosition).id;
        return childrens.get(groudId).get(childPosition);
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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_group_shopping, null);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        final GroupInfo groupInfo = groupInfos.get(groupPosition);
        viewHolder.tvGroupShopName.setText(groupInfo.name);
        viewHolder.cbGroup.setChecked(groupInfo.isChoosed);
        if (groupInfo.isEditor) {
            viewHolder.tvGroupEdit.setText("完成");
        } else {
            viewHolder.tvGroupEdit.setText("编辑");
        }
        viewHolder.cbGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupInfo.isChoosed = ((CheckBox)v).isChecked();
                if (mGroupListener != null) {
                    mGroupListener.onClickGroupCheckBox(groupPosition,((CheckBox)v).isChecked());
                }
            }
        });
        viewHolder.tvGroupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupInfo.isEditor = !groupInfo.isEditor;
                notifyDataSetChanged();
//                if (mGroupListener != null) {
//                    mGroupListener.onClickGroupEdit(groupPosition,groupInfo.isEditor);
//                }
            }
        });
        Log.d("tag","Group convertView is null : " + (convertView == null));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_child_shopping, null);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        GroupInfo groupInfo = groupInfos.get(groupPosition);
        final GoodsInfo goodsInfo = childrens.get(groupInfo.id).get(childPosition);
        if (groupInfo.isEditor) {
            viewHolder.rlProductInfo.setVisibility(View.GONE);
            viewHolder.rlProductEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlProductEdit.setVisibility(View.GONE);
            viewHolder.rlProductInfo.setVisibility(View.VISIBLE);
        }
        if (goodsInfo != null) {
            viewHolder.tvShopName.setText(goodsInfo.desc);
            viewHolder.ivProduct.setImageResource(goodsInfo.goodsImg);
            //viewHolder.tvSingleProductCount.setText(goodsInfo.count);
            viewHolder.tvProductCount.setText("x" + goodsInfo.count);
            viewHolder.tvProductColor.setText(goodsInfo.color);
            viewHolder.tvProductSize.setText(goodsInfo.size);
            viewHolder.tvProductPrice.setText(goodsInfo.price + "");
            SpannableString spannableString = new SpannableString("¥" + goodsInfo.discountPrice);
            StrikethroughSpan span = new StrikethroughSpan();
            spannableString.setSpan(span,0,String.valueOf(goodsInfo.discountPrice).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (viewHolder.tvProductDiscount.getText().toString().length() > 0) {
                viewHolder.tvProductDiscount.setText("");
            }
            viewHolder.tvProductDiscount.setText(spannableString);
            viewHolder.cbChild.setChecked(goodsInfo.isChoosed);

            viewHolder.cbChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.isChoosed = ((CheckBox)v).isChecked();
                    if (mChildListener != null) {
                        mChildListener.onClickChildCheckBox(groupPosition,childPosition);
                    }
                }
            });
            viewHolder.tvProductLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChildListener != null) {
                        mChildListener.onClickLess(groupPosition,childPosition, (TextView) v,goodsInfo.isChoosed);
                    }
                }
            });
            viewHolder.tvProductAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mChildListener != null) {
                        mChildListener.onClickAdd(groupPosition,childPosition, (TextView) v,goodsInfo.isChoosed);
                    }
                }
            });
            viewHolder.tvProductEditDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(context)
                            .setTitle("操作提示")
                            .setMessage("您确定要删除该商品吗？？？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mChildListener != null) {
                                        mChildListener.onClickChildDelete(groupPosition,childPosition);
                                    }
                                }
                            })
                            .create().show();

                }
            });
        }
        Log.d("tag","Children convertView is null : " + (convertView == null));
        return convertView;
    }

    class GroupViewHolder {

        @Bind(R.id.cb_group)
        CheckBox cbGroup;
        @Bind(R.id.tv_group_shop_name)
        TextView tvGroupShopName;
        @Bind(R.id.tv_group_edit)
        TextView tvGroupEdit;

        public GroupViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    class ChildViewHolder {
        @Bind(R.id.cb_child)
        CheckBox cbChild;
        @Bind(R.id.iv_product)
        ImageView ivProduct;
        @Bind(R.id.tv_shop_name)
        TextView tvShopName;
        @Bind(R.id.tv_color)
        TextView tvColor;
        @Bind(R.id.tv_product_color)
        TextView tvProductColor;
        @Bind(R.id.tv_size)
        TextView tvSize;
        @Bind(R.id.tv_product_size)
        TextView tvProductSize;
        @Bind(R.id.tv_product_price)
        TextView tvProductPrice;
        @Bind(R.id.tv_product_discount)
        TextView tvProductDiscount;
        @Bind(R.id.tv_product_count)
        TextView tvProductCount;
        @Bind(R.id.rl_product_info)
        RelativeLayout rlProductInfo;
        @Bind(R.id.tv_product_less)
        TextView tvProductLess;
        @Bind(R.id.tv_singleProductCount)
        TextView tvSingleProductCount;
        @Bind(R.id.tv_product_add)
        TextView tvProductAdd;
        @Bind(R.id.tv_product_edit_color1)
        TextView tvProductEditColor1;
        @Bind(R.id.tv_product_edit_color)
        TextView tvProductEditColor;
        @Bind(R.id.tv_product_edit_size1)
        TextView tvProductEditSize1;
        @Bind(R.id.tv_product_edit_size)
        TextView tvProductEditSize;
        @Bind(R.id.tv_product_edit_delete)
        TextView tvProductEditDelete;
        @Bind(R.id.rl_product_edit)
        RelativeLayout rlProductEdit;

        public ChildViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
