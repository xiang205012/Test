package com.xiang.testapp.shoppingCart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiang.testapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gordon on 2016/10/24.
 */

public class ShopCartActivity extends Activity implements ShopCartAdapter.OnGroupClickListener, ShopCartAdapter.OnChildClickListener {

    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.ll_main_result)
    LinearLayout llMainResult;
    @Bind(R.id.ll_empty)
    LinearLayout llEmpty;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_goodsCount)
    TextView tvGoodsCount;
    @Bind(R.id.tv_cartEdit)
    TextView tvCartEdit;
    @Bind(R.id.elv_listView)
    ExpandableListView listView;
    @Bind(R.id.cb_checkAll)
    CheckBox cbCheckAll;
    @Bind(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @Bind(R.id.tv_transportInfo)
    TextView tvTransportInfo;
    @Bind(R.id.tv_goPay)
    TextView tvGoPay;
    @Bind(R.id.ll_showResult)
    LinearLayout llShowResult;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.tv_clearCart)
    TextView tvClearCart;
    @Bind(R.id.tv_collection)
    TextView tvCollection;
    @Bind(R.id.tv_deleteCart)
    TextView tvDeleteCart;
    @Bind(R.id.ll_sccd)
    LinearLayout llSccd;

    private boolean isClickCartEdit = false;
    private List<GroupInfo> groups = new ArrayList<>();
    private Map<String ,List<GoodsInfo>> children = new HashMap<>();
    private ShopCartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        ButterKnife.bind(this);

        initDatas();
        initEvents();

    }

    private void initDatas() {
        int[] imgs = {R.drawable.goods1,R.drawable.goods2,R.drawable.goods3,
                R.drawable.goods4,R.drawable.goods5,R.drawable.goods6};
        for (int i = 0; i < 3; i++) {
            groups.add(new GroupInfo(i + "","天猫商城" + (i + 1) + "号店"));
            List<GoodsInfo> products = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                products.add(new GoodsInfo(j + "","商品",
                                groups.get(i).name + "的第" + (j + 1) + "个商品",
                                12.0 + new Random().nextInt(23),
                                new Random().nextInt(5) + 1,
                                "豪华","1",imgs[i * j],
                                6.0 + new Random().nextInt(13)));
            }
            children.put(groups.get(i).id,products);
        }
    }

    private void initEvents() {
        adapter = new ShopCartAdapter(this,groups,children);
        adapter.setOnGroupClickListener(this);
        adapter.setOnChildClickListener(this);
        Log.d("tag"," groupCount : " + adapter.getGroupCount());
        listView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    @OnClick({R.id.iv_back,R.id.tv_cartEdit,R.id.cb_checkAll,
            R.id.tv_goPay,R.id.tv_share_friends,R.id.tv_clearCart,
            R.id.tv_collection,R.id.tv_deleteCart})
    public void click(View view){
        switch(view.getId()){
            case R.id.iv_back:

                break;
            case R.id.tv_cartEdit:
                tvCartEdit.setText(isClickCartEdit == true ? "完成" : "编辑");
                llShowResult.setVisibility(isClickCartEdit == true ? View.GONE : View.VISIBLE);
                llSccd.setVisibility(isClickCartEdit == true ? View.VISIBLE : View.GONE);
                isClickCartEdit = !isClickCartEdit;
                break;
            case R.id.cb_checkAll:
                checkAll();
                break;
            case R.id.tv_goPay:

                break;
            case R.id.tv_share_friends:

                break;
            case R.id.tv_clearCart:

                break;
            case R.id.tv_collection:

                break;
            case R.id.tv_deleteCart:
                doDeleteCart();
                break;
        }
    }

    private void doDeleteCart() {
        List<GroupInfo> deleteGroups = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            GroupInfo groupInfo = groups.get(i);
            String id = groupInfo.id;
            if (groupInfo.isChoosed) {
                deleteGroups.add(groupInfo);
            }
            List<GoodsInfo> deleteGoods = new ArrayList<>();
            List<GoodsInfo> goodsInfos = children.get(id);
            for (int j = 0; j < goodsInfos.size(); j++) {
                GoodsInfo goodsInfo = goodsInfos.get(j);
                if (goodsInfo.isChoosed) {
                    deleteGoods.add(goodsInfo);
                }
            }
            goodsInfos.removeAll(deleteGoods);
        }
        groups.removeAll(deleteGroups);
        adapter.notifyDataSetChanged();
        calculate();
    }

    private void checkAll() {
        for (int i = 0; i < groups.size(); i++) {
            onClickGroupCheckBox(i,true);
        }
    }

    @Override
    public void onClickGroupCheckBox(int position, boolean isGroupCheck) {
        GroupInfo groupInfo = groups.get(position);
        List<GoodsInfo> goodsInfos = children.get(groupInfo.id);
        for (int i = 0; i < goodsInfos.size(); i++) {
            goodsInfos.get(i).isChoosed = groupInfo.isChoosed;
        }
        if (isAllCheck()) {
            cbCheckAll.setChecked(true);
        } else {
            cbCheckAll.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void onClickGroupEdit(int position, boolean isGroupEdit) {

    }

    @Override
    public void onClickLess(int groupPosition, int childPosition, TextView tvCount, boolean isChecked) {
        GoodsInfo goodsInfo = children.get(groups.get(groupPosition).id).get(childPosition);
        goodsInfo.count--;
        tvCount.setText(goodsInfo.count);
        if (isChecked) {
            calculate();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickAdd(int groupPosition, int childPosition, TextView tvCount, boolean isChecked) {
        GoodsInfo goodsInfo = children.get(groups.get(groupPosition).id).get(childPosition);
        goodsInfo.count++;
        tvCount.setText(goodsInfo.count);
        if (isChecked) {
            calculate();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickChildCheckBox(int groupPosition, int childPosition) {
        GroupInfo groupInfo = groups.get(groupPosition);
        List<GoodsInfo> goodsInfos = children.get(groupInfo.id);
        for (int i = 0; i < goodsInfos.size(); i++) {
            GoodsInfo goodsInfo = goodsInfos.get(i);
            if (goodsInfo.isChoosed) {
                groupInfo.isChoosed = true;
            } else {
                groupInfo.isChoosed = false;
            }
        }
        if (isAllCheck()) {
            cbCheckAll.setChecked(true);
        } else {
            cbCheckAll.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void onClickChildDelete(int groupPosition, int childPosition) {
        GroupInfo groupInfo = groups.get(groupPosition);
        List<GoodsInfo> goodsInfos = children.get(groupInfo.id);
        goodsInfos.remove(childPosition);
        if (goodsInfos.size() == 0) {
            groups.remove(groupPosition);
        }
        adapter.notifyDataSetChanged();
        setCartNum();
        calculate();
    }

    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            GroupInfo groupInfo = groups.get(i);
            groupInfo.isChoosed = cbCheckAll.isChecked();
            List<GoodsInfo> goodsInfos = children.get(groupInfo.id);
            count += goodsInfos.size();
        }
        if (count == 0) {
            tvGoodsCount.setText("购物车");
        } else {
            tvGoodsCount.setText("购物车(" + count + ")");
        }
        if (groups.size() == 0) {
            clearCart();
        }
    }

    private void clearCart() {
        rlTitle.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        llMainResult.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    private boolean isAllCheck() {
        boolean isAllCheck = false;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).isChoosed) {
                isAllCheck = true;
            } else {
                isAllCheck = false;
            }
        }
        return isAllCheck;
    }


    private void calculate() {
        double totalMoney = 0;
        int totalCount = 0;
        for (int i = 0; i < groups.size(); i++) {
            String id = groups.get(i).id;
            List<GoodsInfo> goodsInfos = children.get(id);
            for (int j = 0; j < goodsInfos.size(); j++) {
                GoodsInfo goodsInfo = goodsInfos.get(j);
                if (goodsInfo.isChoosed) {
                    totalCount++;
                    int count = goodsInfo.count;
                    double price = goodsInfo.price;
                    totalMoney += (price * count);
                }
            }
        }
        tvTotalMoney.setText("¥" + totalMoney);
        tvGoPay.setText("去支付(" + totalCount + ")");
        if (groups.size() == 0) {
            clearCart();
        }
    }

}
