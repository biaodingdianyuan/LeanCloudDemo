package cn.leancloud.chatkit.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;


import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.groupDao;

/**
 * Created by liuhaifeng on 2017/1/5.
 */

public class AddInGroupActivity extends AppCompatActivity {
    private EditText group_name;
    private ImageView query;
    private ListView group_lv;
    private List<groupDao> lists;
    private groupDao dao;
    private groupadapter adapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_addingroup);
        init();
    }

    private void init() {
        group_name = (EditText) findViewById(R.id.groupname);
        query = (ImageView) findViewById(R.id.query);
        back= (ImageView) findViewById(R.id.back_addin);

        group_lv = (ListView) findViewById(R.id.query_group_listview);
        final String name = group_name.getText().toString();
        lists = new ArrayList<groupDao>();
        adapter = new groupadapter(lists);
        group_lv.setAdapter(adapter);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryByName(group_name.getText().toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        group_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupDao group=new groupDao();
                group.setDe(lists.get(position).getDe());
                group.setName(lists.get(position).getName());
                group.setOwenr(lists.get(position).getOwenr());
                group.setId(lists.get(position).getId());
                group.setMember(lists.get(position).getMember());


                startActivity(new Intent(AddInGroupActivity.this,ItemActivity.class).putExtra("group",group));
            }
        });


    }


    public void QueryByName(final String names) {

        AVIMClient client = AVIMClient.getInstance(LCIMData.name);
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    AVIMConversationQuery query = avimClient.getQuery();
                    query.whereEqualTo("name", names);
                    query.findInBackground(new AVIMConversationQueryCallback() {
                        @Override
                        public void done(List<AVIMConversation> list, AVIMException e) {

                            if (e == null) {
                                lists.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    dao = new groupDao();
                                    dao.setMember(list.get(i).getMembers());
                                    dao.setName(list.get(i).getName());
                                    dao.setId(list.get(i).getConversationId());
                                    dao.setOwenr(list.get(i).getCreator());
                                    dao.setDe(list.get(i).getCreatedAt()+"");

                                    lists.add(dao);

                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        });

    }

    class groupadapter extends BaseAdapter {
        List<groupDao> l;

        public groupadapter(List<groupDao> list) {
            l = list;
        }

        @Override
        public int getCount() {
            return l.size();
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
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.query_group_item, null);
            TextView name = (TextView) view.findViewById(R.id.query_item_name);
            TextView creator = (TextView) view.findViewById(R.id.query_item_createorname);
            name.setText(l.get(position).getName());
            creator.setText(l.get(position).getOwenr());
            return view;
        }
    }

}


