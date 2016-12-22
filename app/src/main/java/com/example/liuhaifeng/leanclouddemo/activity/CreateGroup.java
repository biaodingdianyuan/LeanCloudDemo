package com.example.liuhaifeng.leanclouddemo.activity;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.liuhaifeng.leanclouddemo.CustomUserProvider;
import com.example.liuhaifeng.leanclouddemo.GroupOpenHelper;
import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;
import com.example.liuhaifeng.leanclouddemo.groupDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by liuhaifeng on 2016/12/21.
 */

public class CreateGroup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText group_name;
    private ListView add_lv;
    private Button create;
    private ImageView back;
    private List<LCChatKitUser> list;
    private friendAdapter adapter;
    CustomUserProvider customuser;
    private List<String> friend;
    private GroupOpenHelper openHelper;
    private SQLiteDatabase db;
    private StringBuilder sb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        openHelper=new GroupOpenHelper(this,"db",null,1);
        init();

    }

    private void init() {
        group_name= (EditText) findViewById(R.id.group_name);
        add_lv= (ListView) findViewById(R.id.add_friend);
        create= (Button) findViewById(R.id.create);
        back= (ImageView) findViewById(R.id.back_creategroup);
        create.setOnClickListener(this);
        back.setOnClickListener(this);
        list=new ArrayList<LCChatKitUser>();
        customuser=new CustomUserProvider();
        list=CustomUserProvider.getInstance().getAllUsers();
       // list=customuser.getAllUsers();
        adapter=new friendAdapter(list);
        add_lv.setAdapter(adapter);
        add_lv.setOnItemClickListener(this);
        friend=new ArrayList();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_creategroup:
                finish();
                break;
            case R.id.create:
                // TODO: 2016/12/21 创建群组方法
                AVIMClient tom = AVIMClient.getInstance("Tom");
                final Map<String, Object> attr = new HashMap<String, Object>();
                attr.put("type", 1);
                tom.open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if(e==null){
                                avimClient.createConversation(friend,attr, new AVIMConversationCreatedCallback() {
                                    @Override
                                    public void done(AVIMConversation avimConversation, AVIMException e) {
                                        if (e == null) {
                                            MyleancloudAPP app=new MyleancloudAPP();
                                            groupDao dao=new groupDao();
                                            dao.setName(group_name.getText().toString());

                                            //将群组添加到数据库
                                            db=openHelper.getWritableDatabase();
                                            ContentValues values=new ContentValues();
                                            values.put("groupname",group_name.getText().toString());
                                            values.put("number",friend.size());
                                            db.insert("groups",null,values);


                                            Intent intent = new Intent(CreateGroup.this, LCIMConversationActivity.class);
                                            intent.putExtra(LCIMConstants.PEER_ID,avimConversation+"");
                                            startActivity(intent);

                                            Toast.makeText(CreateGroup.this,"创建成功",Toast.LENGTH_SHORT).show();
                                            finish();
//                                            AVIMTextMessage msg = new AVIMTextMessage();
//                                            msg.setText("你们在哪儿？");
//                                            // 发送消息
//                                            avimConversation.sendMessage(msg, new AVIMConversationCallback() {
//
//                                                @Override
//                                                public void done(AVIMException e) {
//                                                    if (e == null) {
//                                                        Log.d("Tom & Jerry", "发送成功！");
//                                                    }
//                                                }
//                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });


                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

     for(int e=0;e<=friend.size();e++){
         if(friend.size()==0){
             friend.add(list.get(i).getUserId());
             return;
         }else {
             if (list.get(i).getUserName().equals(friend.get(e))) {
                 return;
             } else {

                 friend.add(list.get(i).getUserId());
                 return;
             }
         }
     }





    }

    class friendAdapter extends BaseAdapter{
        List<LCChatKitUser> list;
        public friendAdapter(List<LCChatKitUser> list) {
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.item_group,null);
            TextView name_tv= (TextView) view.findViewById(R.id.group_name);
            name_tv.setText(list.get(i).getUserName());
            return view;
        }
    }
}
