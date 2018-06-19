package com.example.administrator.lab3;

/**
 * Created by Administrator on 2017/10/29.
 */

public class Image {
    static int getIm(String str) {
        switch (str) {
            case "Enchated Forest":
                return R.mipmap.i0;
            case "Arla Milk":
                return R.mipmap.i1;
            case "Devondale Milk":
                return R.mipmap.i2;
            case "Kindle Oasis":
                return R.mipmap.i3;
            case "Waitrose 早餐麦片":
                return R.mipmap.i4;
            case "Mcvitie's 饼干":
                return R.mipmap.i5;
            case "Ferrero Rocher":
                return R.mipmap.i6;
            case "Maltesers":
                return R.mipmap.i7;
            case "Lindt":
                return R.mipmap.i8;
            case "Borggreve":
                return R.mipmap.i9;
            default:return R.mipmap.i0;
        }
    }
}
