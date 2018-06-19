package com.example.administrator.lab3;

/**
 * Created by Administrator on 2017/10/21.
 */

public class Items {
    private String name;
    private String info;
    private String price;

    public Items(String name,String price,String info){
        this.name=name;
        this.price=price;
        this.info=info;
    }

    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    public String getInfo(){
        return info;
    }




}
