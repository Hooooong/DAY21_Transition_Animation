package com.hooooong.transitionanimation;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Android Hong on 2017-10-05.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<String> data;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String textData = data.get(position);
        holder.setTextId(position);
        holder.setTextName(textData);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<String> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView textId, textName;

        public Holder(final View itemView) {
            super(itemView);
            textId = (TextView)itemView.findViewById(R.id.textId);
            textName = (TextView)itemView.findViewById(R.id.textName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    // 1. Activity Theme 에 정의하는 것
                    // <item name="android:windowContentTransitions">true</item>
                    // 2. Activity.java 에 Code 로 정의하는 것
                    // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

                    // 단일 요소 설정
                    //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), textName, "textName");

                    // 다중 요소 설정
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(textId, "textId");
                    pairs[1] = new Pair<View, String>(textName, "textName");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), pairs);

                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("textId", textId.getText());
                    intent.putExtra("textName", textName.getText());

                    view.getContext().startActivity(intent, options.toBundle());
                }
            });
        }

        public void setTextId(int position){
            textId.setText(position+"");
        }
        public void setTextName(String data){
            textName.setText("데이터 : " + data);
        }
    }
}
