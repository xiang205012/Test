package com.test.mytest.testTaobaoSortList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mytest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/13.
 */
public class TaobaoSortListActivity extends Activity implements ShopCartAdapter.CheckListener {


    @InjectView(R.id.tv_sort_top)
    TextView title;
    @InjectView(R.id.tv_sort_edtor)
    TextView tv_edtor;
    @InjectView(R.id.tv_sort_money)
    TextView tv_money;
    @InjectView(R.id.tv_go_to_pay)
    TextView tv_pay;
    @InjectView(R.id.cb_sort_all)
    CheckBox allCheckBox;
    @InjectView(R.id.sort_listView)
    ExpandableListView listView;

    @InjectView(R.id.rl_pay)
    RelativeLayout rl_pay;
    @InjectView(R.id.ll_edtor_delete)
    LinearLayout ll_delete;

    @InjectView(R.id.btn_save)
    Button btn_save;
    @InjectView(R.id.btn_share)
    Button btn_share;
    @InjectView(R.id.btn_delete_all)
    Button btn_delete_all;

    @InjectView(R.id.rl_cart)
    RelativeLayout rl_cart;
    @InjectView(R.id.layout_cart_empty)
    FrameLayout layout_cart_empty;

    /**购买商品的总价*/
    private float totalPrice = 0.00f;
    /**购买商品的总数量*/
    private int totalCount = 0;
    /**所有店铺*/
    private List<StoreInfo> groups = new ArrayList<>();
    /**所有商品 通过groups的id为key来存储该商店的所有的商品 为兼容低版本要使用v4包下的ArrayMap*/
    private ArrayMap<String,List<GoodsInfo>> children = new ArrayMap<>();
/** SparseArray和ArrayMap都差不多，使用哪个呢？ 为性能优化避免使用hashMap
    假设数据量都在千级以内的情况下：
    1、如果key的类型已经确定为int类型，那么使用SparseArray，因为它避免了自动装箱的过程，
       如果key为long类型，它还提供了一个LongSparseArray来确保key为long类型时的使用
    2、如果key类型为其它的类型，则使用ArrayMap */

    private int flag = 0;

    private ShopCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobaosort_list);
        ButterKnife.inject(this);
        String string = getResources().getString(R.string.heji);
        String string2 = String.format(string,23.5);
        Spanned string3 = Html.fromHtml(string2);
        tv_money.setText(string3);
        initDatas();
        initEvents();

    }

    private void initEvents() {
        adapter = new ShopCartAdapter(groups,children,this);
        listView.setAdapter(adapter);
        adapter.setCheckListener(this);
        for(int i = 0;i < adapter.getGroupCount();i++){
            // 初始化ExpandableListView默认是展开状态
            listView.expandGroup(i);
        }
    }

    /**
     * 模拟数据：
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，
     * 对应的组元素下辖的子元素被放在Map中，
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {
        for(int i = 0; i < 3;i++){
            groups.add(new StoreInfo(i+"","天猫店铺"+(i+1)+"号店"));
            List<GoodsInfo> products = new ArrayList<>();
            for(int j = 0;j <= i;j++){
                int[] img = {R.drawable.goods1,R.drawable.goods2,
                        R.drawable.goods3,R.drawable.goods4,
                        R.drawable.goods5,R.drawable.goods6};
                products.add(new GoodsInfo(j+"",
                        "商品",
                        groups.get(i).getName()+"的第"+(j+1)+"个商品",
                        12.00 + new Random().nextInt(23),
                        new Random().nextInt(5) + 1,
                        "豪华",
                        "1",
                        img[i*j],
                        6.00 + new Random().nextInt(13)
                        ));
            }
            // 将组元素的一个唯一值，这里取Id，作为子元素List的Key
            children.put(groups.get(i).getId(), products);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    /**设置购物车产品数量*/
    private void setCartNum() {
        int count = 0;
        for(int i = 0;i < groups.size();i++){
            StoreInfo group = groups.get(i);
            // 初始化每个group的checkBox选择状态
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> childs = children.get(group.getId());
            for(GoodsInfo goodsInfo : childs){
                count += 1 ;
            }
        }
        title.setText("购物车(" + count + ")");
        // 说明购物车是清空状态
        if(count == 0){
            title.setText("购物车");
            clearCart();
            return;
        }
        setPayAndMoneyText();
    }

    private void clearCart() {
        tv_edtor.setVisibility(View.GONE);
        rl_cart.setVisibility(View.GONE);
        layout_cart_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void checkGroup(int groupPosition, boolean isCheckGroup) {
        StoreInfo info = groups.get(groupPosition);
        List<GoodsInfo> list = children.get(info.getId());
        for(GoodsInfo goodsInfo : list){
            goodsInfo.setChoosed(isCheckGroup);
        }
        if(isAllCheck()){
            allCheckBox.setChecked(true);
        }else{
            allCheckBox.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();// 统计操作，计算价钱
    }
    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00f;
        for(StoreInfo storeInfo : groups){
            List<GoodsInfo> goodsInfos = children.get(storeInfo.getId());
            for(GoodsInfo goodsInfo : goodsInfos){
                if(goodsInfo.isChoosed()){
                    totalCount += 1;
                    totalPrice += goodsInfo.getPrice()*goodsInfo.getCount();
                }
            }
        }
        setPayAndMoneyText();

    }
    /**给总价和计算数量赋值*/
    private void setPayAndMoneyText() {
        String count = getResources().getString(R.string.jiesuan);
        tv_pay.setText(String.format(count,totalCount));
        String money = getResources().getString(R.string.heji);
        String mon = String.format(money, totalPrice);
        Spanned price = Html.fromHtml(mon);
        tv_money.setText(price);
    }
    /**
     * 1.获得该子元素所在组
     * 2.判断该组下的子元素是否全部选中，全选中更新组元素选中状态
     * 3.检查是否所有组元素都被选中，都被选中更新底部全选checkBox状态
     * */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isCheckChild) {
        StoreInfo storeInfo = groups.get(groupPosition);
        List<GoodsInfo> list = children.get(storeInfo.getId());
        GoodsInfo good = list.get(childPosition);
        boolean isChildAllSameState = true;//判断是否所有的子元素都选中了
        for(GoodsInfo goodsInfo : list){
            if(goodsInfo.isChoosed() != isCheckChild){
                isChildAllSameState = false;
                break;
            }
        }
        if(isChildAllSameState){// 如果是全选中了，组元素更新为选中
            storeInfo.setChoosed(true);
        }else{
            storeInfo.setChoosed(false);
        }
        if(isAllCheck()){
            allCheckBox.setChecked(true);
        }else {
            allCheckBox.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void countAdd(int groupPosition, int childPosition, TextView showCountView, boolean isChecked) {
        StoreInfo storeInfo = groups.get(groupPosition);
        GoodsInfo goodsInfo = children.get(storeInfo.getId()).get(childPosition);
        int count = goodsInfo.getCount();
        if(isChecked){// 如果该子元素是选中状态，加减都要进行统计操作
            count += 1;
            showCountView.setText(""+count);
            calculate();
        }else{// 如果不是选中状态就仅仅是数量变化
            count += 1;
            showCountView.setText(""+count);
            goodsInfo.setCount(count);// 当notifyDataSetChange时会给前一个页面的数量赋值 x + count
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void countSub(int groupPosition, int childPosition, TextView showCountView, boolean isChecked) {
        StoreInfo storeInfo = groups.get(groupPosition);
        GoodsInfo goodsInfo = children.get(storeInfo.getId()).get(childPosition);
        int count = goodsInfo.getCount();
        if(isChecked){// 如果该子元素是选中状态，加减都要进行统计操作
            count = count > 1 ? (count -= 1) : 1;
            showCountView.setText(""+count);
            calculate();
        }else{// 如果不是选中状态就仅仅是数量变化
            count = count > 1 ? (count -= 1) : 1;
            showCountView.setText(""+count);
            goodsInfo.setCount(count);// 当notifyDataSetChange时会给前一个页面的数量赋值 x + count
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 1.获得该子元素，并获得该子元素所在的组元素
     * 2.判断组元素下是否还有子元素，如果没有把组元素也删了
     * 3.判断整个listView中是否还有组元素，如果没有替换ui界面
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        StoreInfo storeInfo = groups.get(groupPosition);
        List<GoodsInfo> goodsInfos = children.get(storeInfo.getId());
        goodsInfos.remove(goodsInfos.get(childPosition));
        if (goodsInfos.size() == 0){
            groups.remove(storeInfo);
        }
        if(groups.size() == 0){
            clearCart();
            return;
        }
        setCartNum();
        adapter.notifyDataSetChanged();
        calculate();
    }

    /**检查是否为全选中*/
    public boolean isAllCheck() {
        for(StoreInfo storeInfo : groups){
            if(!storeInfo.isChoosed()){
                return false;
            }
        }
        return true;
    }
}
