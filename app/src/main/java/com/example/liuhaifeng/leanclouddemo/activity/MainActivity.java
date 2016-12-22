package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liuhaifeng.leanclouddemo.R;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private LinearLayout talk, person;
    private TextView title,back,group;
    private ContactFragment contactFragment;
    private LCIMConversationListFragment lcimConversationListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
        toolbar.setBackgroundColor(Color.CYAN);
        title.setText("会话");


        lcimConversationListFragment= new LCIMConversationListFragment();
        contactFragment = new ContactFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, lcimConversationListFragment).commit();
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        talk = (LinearLayout) findViewById(R.id.talk);
        person = (LinearLayout) findViewById(R.id.person);
        title= (TextView) findViewById(R.id.title_main);
        back= (TextView) findViewById(R.id.back);
        group= (TextView) findViewById(R.id.group);
        back.setOnClickListener(this);
        group.setOnClickListener(this);

        talk.setOnClickListener(this);
        person.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,contactFragment).commit();
                title.setText("联系人");

                group.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                person.setBackgroundColor(Color.GRAY);
                talk.setBackgroundColor(Color.WHITE);
                break;
            case R.id.talk:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, lcimConversationListFragment).commit();
                title.setText("会话");
                person.setBackgroundColor(Color.WHITE);
                talk.setBackgroundColor(Color.GRAY);
                group.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);

                break;
            case R.id.back:
             finish();
                break;
            case R.id.group:
                // TODO: 2016/12/21 添加群组列表界面
                startActivity(new Intent(MainActivity.this,GroupListActivity.class));

                break;
        }
    }
}
