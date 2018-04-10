package com.hosigus.demo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyRecycleViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        adapter = new MyRecycleViewAdapter(new ArrayList<String>(), MainActivity.this);
        rv.setAdapter(adapter);
        final SwipeRefreshLayout srl = findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
                srl.setRefreshing(false);
            }
        });
        NetUtils.requestNet(1, new NetUtils.NetCallBack() {
            @Override
            public void connectOK(List<String> urlList) {
                adapter.add(urlList);
            }

            @Override
            public void connectFail(String res) {

            }
        });
    }
}
