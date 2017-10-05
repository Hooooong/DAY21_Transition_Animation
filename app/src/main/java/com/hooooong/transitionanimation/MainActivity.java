package com.hooooong.transitionanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        CustomAdapter customAdapter = new CustomAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);

        customAdapter.setData(setData());
    }

    private List<String> setData(){
        List<String> data = new ArrayList<>();

        for(int i = 0; i<100; i++){
            data.add(i+"");
        }
        return data;
    }
}
