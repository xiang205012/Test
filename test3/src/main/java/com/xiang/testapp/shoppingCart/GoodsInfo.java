package com.xiang.testapp.shoppingCart;

/**
 * Created by gordon on 2016/10/24.
 */

public class GoodsInfo {

    public String id;
    public String name;
    public String imageUrl;
    public boolean isChoosed;
    public String desc;
    public double price;
    public int count;
    public int position;
    public String color;
    public String size;
    public int goodsImg;
    public double discountPrice;

    public GoodsInfo(String id,String name,String desc,double price,
                     int count,String color,String size,int goodsImg,double discountPrice){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.count = count;
        this.color = color;
        this.size = size;
        this.goodsImg = goodsImg;
        this.discountPrice = discountPrice;


    }




}
