package com.example.administrator.lab9.service;

import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.model.Repos;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/24.
 */
//@Path：URL占位符，用于替换和动态更新,相应的参数必须使用相同的字符串被@Path进行注释
public interface GithubService {
    
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);

    @GET("/users/{user}/repos")
    Observable<ArrayList<Repos>> getRepos(@Path("user") String user);

}
