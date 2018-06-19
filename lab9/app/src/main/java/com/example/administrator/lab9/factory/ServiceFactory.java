package com.example.administrator.lab9.factory;

import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.model.Repos;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/24.
 */

public class ServiceFactory {

    public static Retrofit createRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)//设置baseUrl
                .addConverterFactory(GsonConverterFactory.create())//添加Gson转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//Rx JavaCall Adapter
                .client(createOkHttp())//设置OKHttpClient,如果不设置会提供一个默认的
                .build();
    }

    public static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .readTimeout(30, TimeUnit.SECONDS)//读超时
                .writeTimeout(10, TimeUnit.SECONDS)//写超时
                .retryOnConnectionFailure(true)    //是否自动重连
                .build();
        return okHttpClient;
    }

}
