package com.example.administrator.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.lab9.Adapter.CommonAdapter;
import com.example.administrator.lab9.factory.ServiceFactory;
import com.example.administrator.lab9.model.Repos;
import com.example.administrator.lab9.service.GithubService;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/24.
 */

public class ReposActivity extends AppCompatActivity {
    private static String BASE_URL="https://api.github.com";
    private ArrayList<Repos> detail_card=new ArrayList<Repos>();
    private CommonAdapter.SecondAdapter detail_cardAdapter;
    private RecyclerView detail_recyclerView;
    private ProgressBar detail_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        detail_progressBar=(ProgressBar)findViewById(R.id.detailProgress);
        detail_recyclerView=(RecyclerView)findViewById(R.id.detailList);

        detail_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter c=new CommonAdapter();
        detail_cardAdapter = c.new SecondAdapter(detail_card, ReposActivity.this);
        detail_recyclerView.setAdapter(detail_cardAdapter);

        detail_progressBar.setVisibility(View.VISIBLE);
        

        Retrofit GithubRetrofit = ServiceFactory.createRetrofit(BASE_URL);
        GithubService service = GithubRetrofit.create(GithubService.class);
        String search= getIntent().getStringExtra("login");
        service.getRepos(search)               //获取Observable对象
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Repos>>() {
                    @Override
                    public void onCompleted() {
                        detail_progressBar.setVisibility(View.INVISIBLE);
                        System.out.print("完成传输");
                    }

                    @Override
                    public void onError(Throwable e) {
                        detail_progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ReposActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                        Log.e("Github-Demo",e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Repos> repos) {
                            detail_card.addAll(repos);
                        detail_cardAdapter.notifyDataSetChanged();
                    }
                });

    }

}
