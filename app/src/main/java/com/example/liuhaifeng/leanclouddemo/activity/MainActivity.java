package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.liuhaifeng.leanclouddemo.R;

import cn.leancloud.chatkit.activity.AddInGroupActivity;
import cn.leancloud.chatkit.activity.ContactFragment;
import cn.leancloud.chatkit.activity.CreateGroup;
import cn.leancloud.chatkit.activity.GroupListFragment;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private LinearLayout talk, person,group;
    private TextView title,tv_huihua,tv_people,tv_group;
    private ImageView add,imghuihua,imgpeople,imggroup;
    private ContactFragment contactFragment;
    private LCIMConversationListFragment lcimConversationListFragment;
    private GroupListFragment groupListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        title.setText("会话");
        change(1);
        lcimConversationListFragment= new LCIMConversationListFragment();
        contactFragment = new ContactFragment();
        groupListFragment=new GroupListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, lcimConversationListFragment).commit();
    }
    public void init() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        talk = (LinearLayout) findViewById(R.id.talk);
        person = (LinearLayout) findViewById(R.id.person);
        title= (TextView) findViewById(R.id.title_main);
        tv_huihua= (TextView) findViewById(R.id.tv_huihua);
        imghuihua= (ImageView) findViewById(R.id.img_tuihua);
        tv_group= (TextView) findViewById(R.id.tv_group);
        imggroup= (ImageView) findViewById(R.id.img_group);
        tv_people= (TextView) findViewById(R.id.tv_people);
        imgpeople= (ImageView) findViewById(R.id.img_people);
        group= (LinearLayout) findViewById(R.id.grou);
        add= (ImageView) findViewById(R.id.add_group);
        group.setOnClickListener(this);
        talk.setOnClickListener(this);
        person.setOnClickListener(this);
        add.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,contactFragment).commit();
                title.setText("联系人");
                add.setVisibility(View.GONE);
                change(2);
                break;
            case R.id.talk:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, lcimConversationListFragment).commit();
                title.setText("会话");
                change(1);
                add.setVisibility(View.GONE);
                break;
            case R.id.grou:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,groupListFragment).commit();
                title.setText("群组");
                add.setVisibility(View.VISIBLE);
                change(3);
                break;
            case R.id.add_group:
                popupwindow(view);
                break;

        }
    }
    public void popupwindow(View v) {
        View view = getLayoutInflater().inflate(R.layout.popupwindow_group, null);
        TextView create_group = (TextView) view.findViewById(R.id.create_group);
        TextView add_group1 = (TextView) view.findViewById(R.id.addingroup);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        popupWindow.showAsDropDown(v, toolbar.getWidth() - view.getWidth(), add.getHeight() / 2);
        //创建群组
        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateGroup.class));
                popupWindow.dismiss();

            }
        });
        //加入群组
        add_group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddInGroupActivity.class));
                popupWindow.dismiss();
            }
        });


    }


    public void change(int i){
        switch (i){
            case 1:
                tv_people.setTextColor(getResources().getColor(R.color.black));
                tv_group.setTextColor(getResources().getColor(R.color.black));
                tv_huihua.setTextColor(getResources().getColor(R.color.green));
                imghuihua.setImageDrawable(getResources().getDrawable(R.drawable.huihua_green));
                imgpeople.setImageDrawable(getResources().getDrawable(R.drawable.people_black));
                imggroup.setImageDrawable(getResources().getDrawable(R.drawable.group_black));
                break;
            case 2:
                tv_people.setTextColor(getResources().getColor(R.color.green));
                tv_group.setTextColor(getResources().getColor(R.color.black));
                tv_huihua.setTextColor(getResources().getColor(R.color.black));
                imghuihua.setImageDrawable(getResources().getDrawable(R.drawable.huihua_black));
                imgpeople.setImageDrawable(getResources().getDrawable(R.drawable.people_green));
                imggroup.setImageDrawable(getResources().getDrawable(R.drawable.group_black));
                break;
            case 3:
                tv_people.setTextColor(getResources().getColor(R.color.black));
                tv_group.setTextColor(getResources().getColor(R.color.green));
                tv_huihua.setTextColor(getResources().getColor(R.color.black));
                imghuihua.setImageDrawable(getResources().getDrawable(R.drawable.huihua_black));
                imgpeople.setImageDrawable(getResources().getDrawable(R.drawable.people_black));
                imggroup.setImageDrawable(getResources().getDrawable(R.drawable.group_green));
                break;
        }

    }


}
