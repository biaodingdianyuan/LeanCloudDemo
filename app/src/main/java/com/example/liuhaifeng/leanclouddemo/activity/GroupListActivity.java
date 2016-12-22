package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liuhaifeng.leanclouddemo.GroupOpenHelper;
import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;
import com.example.liuhaifeng.leanclouddemo.groupDao;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;


public class GroupListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView back_group, add_group;
    private ListView lv_group;
    private groupDao dao;
    private List<groupDao> list;
    private groupAdapter adapter;
    private GroupOpenHelper openHelper;
    private SQLiteDatabase db;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        openHelper = new GroupOpenHelper(this, "db", null, 1);
        init();
    }

    public void init() {
        back_group = (ImageView) findViewById(R.id.back_group);
        add_group = (ImageView) findViewById(R.id.add_group);
        lv_group = (ListView) findViewById(R.id.group_lv);
        back_group.setOnClickListener(this);
        add_group.setOnClickListener(this);
        //
        list = new ArrayList<groupDao>();
        list = getgroup();
        adapter = new groupAdapter(list);
        lv_group.setAdapter(adapter);

        // TODO: 2016/12/21 lv的点击事件
        lv_group.setOnItemClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_group:
                finish();
                break;
            case R.id.add_group:
                // TODO: 创建群组操作
                startActivity(new Intent(GroupListActivity.this, CreateGroup.class));

                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO: 2016/12/21 获取数据跳转到会话界面

        Intent intent = new Intent(GroupListActivity.this, LCIMConversationActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, list.get(i).getName());
        startActivity(intent);


    }

    class groupAdapter extends BaseAdapter {
        List<groupDao> list;

        public groupAdapter(List<groupDao> list) {
            this.list = list;
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
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_group, null);
            TextView group_name = (TextView) view.findViewById(R.id.group_name);
            group_name.setText(list.get(i).getName());
            return view;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        list.addAll(getgroup());
        adapter.notifyDataSetChanged();

    }



    public List<groupDao> getgroup() {
        db = openHelper.getWritableDatabase();
        sb = new StringBuilder();
        List<groupDao>  list_o=new ArrayList<groupDao>();
        Cursor cursor = db.query("groups", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int pid = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("groupname"));
                int number = cursor.getInt(cursor.getColumnIndex("number"));
                //sb.append("id：" + pid + "：" + name + "\n");
               dao=new groupDao();
                dao.setName(name);
                dao.setNum(number);

                list_o.add(dao);
            } while (cursor.moveToNext());
        }
        cursor.close();


        return list_o;
    }
}
