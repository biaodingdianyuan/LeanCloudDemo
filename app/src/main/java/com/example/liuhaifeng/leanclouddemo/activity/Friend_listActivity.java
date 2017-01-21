package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKitUser;

/**
 * Created by liuhaifeng on 2017/1/21.
 */

public class Friend_listActivity extends AppCompatActivity {
    String gname;
    private ListView groupfriend_lv;
    private List<LCChatKitUser> list_u;
    private LCChatKitUser user;
    private SQLiteDatabase db;
    private TextView OK;
    private Map<Integer, Boolean> ischecked;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        gname = intent.getStringExtra("friendgroupname");
        ischecked = new HashMap<Integer, Boolean>();
        OK = (TextView) findViewById(R.id.add_friendgroup);
        groupfriend_lv = (ListView) findViewById(R.id.group_friend_lv);
        list_u = new ArrayList<LCChatKitUser>();
        adapter = new UserAdapter(list_u);
        groupfriend_lv.setAdapter(adapter);
        getdata();
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ischecked.size(); i++) {
                    if (ischecked.get(i).equals(true)) {
                        db = MyleancloudAPP.friendOpenHelper.getWritableDatabase();
                        ContentValues va = new ContentValues();
                        va.put("fg",gname);
                        db.update("friend", va, "owenr=? and name=?", new String[]{MyleancloudAPP.name, list_u.get(i).getUserName()});

                    }
                }
                finish();
            }
        });

    }

    class UserAdapter extends BaseAdapter {
        private List<LCChatKitUser> list;

        public UserAdapter(List<LCChatKitUser> list) {
            this.list = list;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.groupfriend_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.friend_name);
            final ImageView delete = (ImageView) convertView.findViewById(R.id.friend_delete);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.ischeck);
            name.setText(list.get(position).getUserName());
            ischecked.put(position, false);
            if (list.get(position).getFg().equals(gname)) {
                delete.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = MyleancloudAPP.friendOpenHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("fg", "我的好友");
                    db.update("friend", values, "owenr=? and name=?", new String[]{MyleancloudAPP.name, list.get(position).getUserName()});
                    delete.setVisibility(View.GONE);
                    checkBox.setVisibility(View.VISIBLE);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ischecked.put(position, true);
                    } else {
                        ischecked.put(position, false);
                    }
                }
            });


            return convertView;
        }
    }

    public void getdata() {
        list_u.clear();
        db = MyleancloudAPP.friendOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("friend", null, "owenr=?", new String[]{MyleancloudAPP.name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                user = new LCChatKitUser(
                        cursor.getString(cursor.getColumnIndex("userId"))
                        , cursor.getString(cursor.getColumnIndex("name"))
                        , cursor.getString(cursor.getColumnIndex("avatarUrl"))
                        , cursor.getString(cursor.getColumnIndex("fg"))
                        , cursor.getString(cursor.getColumnIndex("owenr"))
                );
                list_u.add(user);
            } while (cursor.moveToNext());
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getdata();
    }
}
