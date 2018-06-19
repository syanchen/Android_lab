package com.example.administrator.lab3;

/**
 * Created by Administrator on 2017/10/29.
 */

public class MessageEvent {
    private int cnt;
    private String name;
    private String info;
    private String price;

    public MessageEvent(String name,String info,String price,int cnt){
        this.name=name;
        this.info=info;
        this.price=price;
        this.cnt=cnt;
    }

    public String getName(){
        return name;
    }
    public String getInfo(){
        return info;
    }


    public String getPrice(){
        return price;
    }

    public int getCnt(){
        return cnt;
    }
}
