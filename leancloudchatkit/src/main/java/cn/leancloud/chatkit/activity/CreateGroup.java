package cn.leancloud.chatkit.activity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.R;

/**
 * Created by liuhaifeng on 2016/12/21.
 */

public class CreateGroup extends AppCompatActivity  {

    private EditText group_name;
    private ListView add_lv;
    private TextView create;
    private ImageView back;
    private List<LCChatKitUser> list;
    private friendAdapter adapter;

    private List<String> friend;

    private SQLiteDatabase db;
    private StringBuilder sb;
    private HashMap<Integer ,Boolean> ischeck =new HashMap<Integer, Boolean>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        init();

    }

    private void init() {
        group_name= (EditText) findViewById(R.id.group_names);
        add_lv= (ListView) findViewById(R.id.add_friend);
        create= (TextView) findViewById(R.id.create1);
        back= (ImageView) findViewById(R.id.back_creategroup);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVIMClient tom = AVIMClient.getInstance(LCIMData.name);
                for(int i=0;i<ischeck.size();i++){
                    if(ischeck.get(i).equals(true)){
                        friend.add(list.get(i).getUserId());
                    }
                }
                tom.open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if(e==null){
                            avimClient.createConversation(friend,group_name.getText().toString(),null,false,true, new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation avimConversation, AVIMException e) {
                                    if (e == null) {
                                        Toast.makeText(CreateGroup.this,"创建成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list=new ArrayList<LCChatKitUser>();
   //     customuser=new CustomUserProvider();
        list= LCIMData.User_list;
        adapter=new friendAdapter(list);
        add_lv.setAdapter(adapter);
        friend=new ArrayList();
        friend.add(LCIMData.name);
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.item_group,null);
            TextView name_tv= (TextView) view.findViewById(R.id.group_name);
            ImageView img= (ImageView) view.findViewById(R.id.group_head);
            name_tv.setText(list.get(i).getUserName());
            Picasso.with(CreateGroup.this).load(list.get(i).getAvatarUrl()).into(img);
            CheckBox box= (CheckBox) view.findViewById(R.id.isornoq);
            ischeck.put(i,false);
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                  if(isChecked){
                      ischeck.put(i,isChecked);
                  }else{
                        ischeck.put(i,isChecked);
                  }

                }
            });
            return view;


        }
    }
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
}
