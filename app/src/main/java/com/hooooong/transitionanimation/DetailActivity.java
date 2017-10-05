package com.hooooong.transitionanimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView textId;
    private TextView textName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 2. Activity Code 로 정의
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView(){
        String id = null;
        String name = null;
        Intent intent = getIntent();
        if(intent != null){
            id = intent.getStringExtra("textId");
            name = intent.getStringExtra("textName");
        }

        textId = (TextView)findViewById(R.id.textId);
        textName = (TextView)findViewById(R.id.textName);

        textId.setText(id);
        textName.setText(name);
    }
}
