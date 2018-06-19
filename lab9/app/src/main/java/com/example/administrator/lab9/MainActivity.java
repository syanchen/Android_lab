package com.example.administrator.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.lab9.Adapter.CommonAdapter;
import com.example.administrator.lab9.factory.ServiceFactory;
import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.service.GithubService;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.administrator.lab9.factory.ServiceFactory.createRetrofit;

public class MainActivity extends AppCompatActivity {

    private Button clear,fetch;
    private EditText search;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<Github> card;
    private CommonAdapter.FirstAdapter cardAdapter;
    private static String BASE_URL="https://api.github.com";
    private String search_name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Act();
        progressBar.setVisibility(View.INVISIBLE);
        fetch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                
                GithubService service;
                Retrofit GithubRetrofit = ServiceFactory.createRetrofit(BASE_URL);
                service = GithubRetrofit.create(GithubService.class);

                search_name=search.getText().toString();
                service.getUser(search_name)
                        .subscribeOn(Schedulers.newThread())//新建线程进行网络访问
                        .observeOn(AndroidSchedulers.mainThread())//在主线程处理请求结果
                        .subscribe(new Subscriber<Github>() {
                            @Override
                            public void onCompleted() {
                                System.out.println("完成传输");
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this,"请确认你搜索的用户存在",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onNext(Github github) {
                                if(github!=null)
                                    card.add(github);
                                cardAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    public void Act(){
        clear=(Button)findViewById(R.id.clear);
        fetch=(Button)findViewById(R.id.fetch);
        search=(EditText)findViewById(R.id.content);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.main_progress);
        card=new ArrayList<Github>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter commonAdapter=new CommonAdapter();
        cardAdapter = commonAdapter.new FirstAdapter(card, MainActivity.this);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){

            @Override
            public void onClick(int position) {
                Intent intent=new Intent(MainActivity.this,ReposActivity.class);
                intent.putExtra("login",card.get(position).getLogin());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                card.remove(position);
                cardAdapter.notifyItemRemoved(position);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card!=null){
                    card.clear();
                    cardAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}
