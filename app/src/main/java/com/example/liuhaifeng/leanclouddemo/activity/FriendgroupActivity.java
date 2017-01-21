package com.example.liuhaifeng.leanclouddemo.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuhaifeng.leanclouddemo.FriendGroupDao;
import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;

/**
 * Created by liuhaifeng on 2017/1/20.
 */

public class FriendgroupActivity extends AppCompatActivity {
    private List<FriendGroupDao> list;
    private FriendGroupDao dao;
    private ListView lv;
    private ImageView add;
    private FriendgroupAdapter adapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friendgroup);
        init();
    }
    public void init() {
        lv = (ListView) findViewById(R.id.friendgroup_lv);
        list = new ArrayList<FriendGroupDao>();
        adapter = new FriendgroupAdapter(list);
        add = (ImageView) findViewById(R.id.add_friendgroup);
        lv.setAdapter(adapter);
        getdata();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = null;
                AlertDialog.Builder builder = null;
                final EditText editText = new EditText(FriendgroupActivity.this);
                builder = new AlertDialog.Builder(FriendgroupActivity.this);
                alertDialog = builder

                        .setView(editText)
                        .setTitle("请输入" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db = MyleancloudAPP.friendfroupOpenHelper.getWritableDatabase();
                                String s = editText.getText().toString();
                                ContentValues v = new ContentValues();
                                v.put("name", s);
                                v.put("owenr", MyleancloudAPP.name);
                                db.insert("friendgroup", null, v);
                                getdata();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(FriendgroupActivity.this,Friend_listActivity.class).putExtra("friendgroupname",list.get(position).getName()));

            }
        });
    }
    class FriendgroupAdapter extends BaseAdapter {
        private List<FriendGroupDao> list;

        public FriendgroupAdapter(List<FriendGroupDao> list) {
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
            convertView = getLayoutInflater().inflate(R.layout.friendgroup_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.friendgroup_name);
            ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
            name.setText(list.get(position).getName());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getFriends().size() > 0) {
                        Toast.makeText(FriendgroupActivity.this, "该群组内还有好友，不能删除", Toast.LENGTH_SHORT).show();
                    }else{

                        db=MyleancloudAPP.friendfroupOpenHelper.getWritableDatabase();
                        db.delete("friendgroup","owenr=? and name=?",new String[]{MyleancloudAPP.name,list.get(position).getName()});
                        list.remove(position);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       adapter.notifyDataSetChanged();
                                   }
                               });
                            }
                        }).start();
                    }
                }
            });
            return convertView;
        }
    }
    public void getdata() {
        list.clear();

        LCChatKitUser user;
        List<LCChatKitUser> list_u = new ArrayList<LCChatKitUser>();
        db = MyleancloudAPP.friendfroupOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("friendgroup", null, "owenr=?", new String[]{MyleancloudAPP.name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                dao = new FriendGroupDao();
                dao.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(dao);
            } while (cursor.moveToNext());
        }
        db = MyleancloudAPP.friendOpenHelper.getReadableDatabase();

        Cursor c = db.query("friend", null, "owenr=?", new String[]{MyleancloudAPP.name}, null, null, null);
        if (c.moveToFirst()) {
            do {
                user = new LCChatKitUser(
                        c.getString(c.getColumnIndex("userId")),
                        c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("avatarUrl")),
                        c.getString(c.getColumnIndex("fg")),
                        c.getString(c.getColumnIndex("owenr"))
                );
                list_u.add(user);
            } while (c.moveToNext());
        }
        for (int i = 0; i < list.size(); i++) {
            List<LCChatKitUser> l =new ArrayList<LCChatKitUser>();
            for (int o = 0; o < list_u.size(); o++) {
                if (list_u.get(o).getFg().equals(list.get(i).getName())) {
                    user = new LCChatKitUser(
                            list_u.get(o).getUserId()
                            , list_u.get(o).getUserName()
                            , list_u.get(o).getAvatarUrl()
                            , list_u.get(o).getFg()
                            , list_u.get(o).getOwenr()
                    );
                    l.add(user);
                }
            }

           list.get(i).setFriends(l);
        }
        adapter.notifyDataSetChanged();
    }
}
