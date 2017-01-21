package cn.leancloud.chatkit.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVQuery;
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
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by liuhaifeng on 2017/1/13.
 */

public class GroupListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private ImageView back_group, add_group;
    private ListView lv_group;
    private groupDao dao;
    private Toolbar toolbar;
    private List<groupDao> list;
    private groupAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.activity_grouplist,null);
        IntentFilter filter = new IntentFilter();
        filter.addAction("chatting");




        init(view);


        return view;
    }

    public void init(View view) {
      //  add_group = (ImageView) view.findViewById(R.id.add_group);
        lv_group = (ListView) view.findViewById(R.id.group_lv);

        //toolbar = (Toolbar) view.findViewById(R.id.group_toolbar);

        list = new ArrayList<groupDao>();

        adapter = new groupAdapter(list);
        lv_group.setAdapter(adapter);

        refreshGroupList();
        // Toast.makeText(GroupListActivity.this,list.size(),Toast.LENGTH_SHORT).show();
        lv_group.setOnItemClickListener(this);


    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getActivity(), LCIMConversationActivity.class);
        intent.putExtra(LCIMConstants.CONVERSATION_ID,  list.get(i).getId());
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
            view = getActivity().getLayoutInflater().inflate(R.layout.item_group, null);
            ImageView img_head= (ImageView) view.findViewById(R.id.group_head);
            TextView group_name = (TextView) view.findViewById(R.id.group_name);
            CheckBox box= (CheckBox) view.findViewById(R.id.isornoq);
            box.setVisibility(View.GONE);
            group_name.setText(list.get(i).getName());
            return view;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshGroupList();
    }

    private void refreshGroupList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<groupDao> l=new ArrayList<groupDao>();

                AVIMClient tom = AVIMClient.getInstance(LCIMData.name);
                tom.open(new AVIMClientCallback() {

                    @Override
                    public void done(final AVIMClient client, AVIMException e) {
                        if (e == null) {
                            // TODO: 2017/1/10 添加查询条件 排除单聊
                            List li=new ArrayList();
                            li.add(LCIMData.name);
                            AVIMConversationQuery query = client.getQuery();
                            query.containsMembers(li);

                            query.orderByDescending("createdAt");
                            //先从网上获取数据 ，发生网络错误的时候，再从本地查询
                            query.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                            query.findInBackground(new AVIMConversationQueryCallback() {
                                @Override
                                public void done(List<AVIMConversation> convs, AVIMException e) {
                                    groupDao d;
                                    if (e == null) {
                                        list.clear();
                                        for(int i=0;i<convs.size();i++){
                                            d=new groupDao();
                                            d.setId(convs.get(i).getConversationId());
                                            d.setName(convs.get(i).getName());
                                            d.setMember(convs.get(i).getMembers());
                                            if(convs.get(i).getMembers().size()==2){
                                            }else{
                                                list.add(d);
                                            }

                                        }

                                        adapter.notifyDataSetChanged();



                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();




    }
}
