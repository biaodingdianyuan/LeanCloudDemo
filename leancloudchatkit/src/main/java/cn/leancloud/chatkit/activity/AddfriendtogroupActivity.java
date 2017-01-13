package cn.leancloud.chatkit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.R;

/**
 * Created by liuhaifeng on 2017/1/10.
 */
public class AddfriendtogroupActivity extends AppCompatActivity {

    private ImageView back;
    private TextView yaoqing;
    private ListView lv;
    Adapter adapter;
    List friends;
    String CID;
    private HashMap<Integer,Boolean> ischeck;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activityaddfriendtogroup);
        LCIMData.User_list.get(0).getUserName();
        lv= (ListView) findViewById(R.id.addtogroup_lv);
        yaoqing= (TextView) findViewById(R.id.add);
        back= (ImageView) findViewById(R.id.back_af);
        adapter=new Adapter(LCIMData.User_list);
        lv.setAdapter(adapter);
        Intent intent=getIntent();
        CID=intent.getStringExtra("id");
        ischeck=new HashMap<Integer, Boolean>();

        yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friends=new ArrayList();
                for(int i=0;i<ischeck.size();i++){
                    if(ischeck.get(i).equals(true)){
                        friends.add(LCIMData.User_list.get(i).getUserId());
                    }
                }

                AVIMClient jerry = AVIMClient.getInstance(LCIMData.name);
                jerry.open(new AVIMClientCallback() {

                    @Override
                    public void done(AVIMClient client, AVIMException e) {
                        if (e == null) {
                            //登录成功
                            final AVIMConversation conv = client.getConversation(CID);
                            conv.join(new AVIMConversationCallback() {
                                @Override
                                public void done(AVIMException e) {
                                    if (e == null) {
                                        //加入成功
                                        conv.addMembers(friends, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                if(e==null){
                                                    Toast.makeText(AddfriendtogroupActivity.this,"添加好友成功",Toast.LENGTH_SHORT).show();
                                                finish();
                                                }


                                            }
                                        });
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





    }
    class Adapter extends BaseAdapter{
        List<LCChatKitUser> list;
        public Adapter( List<LCChatKitUser> list) {
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view=getLayoutInflater().inflate(R.layout.lcimitem_group,null);
            ImageView img= (ImageView) view.findViewById(R.id.lcgroup_head);
            TextView name= (TextView) view.findViewById(R.id.lcgroup_name);
            CheckBox box= (CheckBox) view.findViewById(R.id.lcisorno);
            Picasso.with(AddfriendtogroupActivity.this).load(list.get(position).getAvatarUrl()).into(img);
            name.setText(list.get(position).getUserName());
            ischeck.put(position,false);
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ischeck.put(position,isChecked);
                    }else{
                        ischeck.put(position,isChecked);
                    }


                }
            });


            return view ;
        }
    }

}
