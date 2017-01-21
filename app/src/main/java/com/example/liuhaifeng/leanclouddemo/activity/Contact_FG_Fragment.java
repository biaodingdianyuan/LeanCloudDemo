package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.liuhaifeng.leanclouddemo.FriendGroupDao;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;


/**
 * Created by liuhaifeng on 2017/1/20.
 */

public class Contact_FG_Fragment extends Fragment {
    private ExpandableListView friendgroup_lv;
    private List<FriendGroupDao> list_fg;
    private FriendGroupDao dao;
    private Contact_fg_Adapter adapter;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_fg, null);
        friendgroup_lv = (ExpandableListView) view.findViewById(R.id.contact_fg_lv);
        list_fg = new ArrayList<FriendGroupDao>();

        //添加数据
        getdata();
        friendgroup_lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getContext(), LCIMConversationActivity.class);
                intent.putExtra(LCIMConstants.PEER_ID, list_fg.get(groupPosition).getFriends().get(childPosition).getUserId());
                getContext().startActivity(intent);


                return true;
            }
        });

        friendgroup_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               startActivity(new Intent(getActivity(),FriendgroupActivity.class));

                return true;
            }
        });


        return view;
    }

    class Contact_fg_Adapter extends BaseExpandableListAdapter {
        private List<FriendGroupDao> list;
        private LayoutInflater layoutInflater;

        public Contact_fg_Adapter(List<FriendGroupDao> list, Context context) {
            this.list = list;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).getFriends().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).getFriends().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.contact_fg_fragment_group_item, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.contact_fg_group_name);
            ImageView img = (ImageView) convertView.findViewById(R.id.contact_fg_img);
            name.setText(list.get(groupPosition).getName());
            if (isExpanded) {
                img.setImageDrawable(getResources().getDrawable(R.drawable.up));
            } else {
                img.setImageDrawable(getResources().getDrawable(R.drawable.left));
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.contact_fg_fragment_child_item, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.contact_fg_child_name);
            ImageView friend_img = (ImageView) convertView.findViewById(R.id.contact_fg_child_img);
            name.setText(list.get(groupPosition).getFriends().get(childPosition).getUserName());
            Picasso.with(getActivity())

                    .load(list.get(groupPosition).getFriends().get(childPosition).getAvatarUrl())
                    .resize(50,50)
                    .into(friend_img);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public void getdata() {
        list_fg.clear();
        List<LCChatKitUser> list_u = new ArrayList<LCChatKitUser>();
        LCChatKitUser user;

        db = MyleancloudAPP.friendfroupOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("friendgroup", null, "owenr=?", new String[]{MyleancloudAPP.name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                dao = new FriendGroupDao();
                dao.setName(cursor.getString(cursor.getColumnIndex("name")));
                list_fg.add(dao);
            } while (cursor.moveToNext());
        }


        db=MyleancloudAPP.friendOpenHelper.getReadableDatabase();
        Cursor c=db.query("friend",null,"owenr=?",new String[]{MyleancloudAPP.name},null,null,null);
        if(c.moveToFirst()){
            do{
                user=new LCChatKitUser(
                        c.getString(c.getColumnIndex("userId"))
                        ,c.getString(c.getColumnIndex("name"))
                        ,c.getString(c.getColumnIndex("avatarUrl"))
                        ,c.getString(c.getColumnIndex("fg"))
                        ,c.getString(c.getColumnIndex("owenr"))
                );
                list_u.add(user);
            }while (c.moveToNext());
        }

        for (int i = 0; i < list_fg.size(); i++) {
            List<LCChatKitUser> l=new ArrayList<LCChatKitUser>();
            for (int u = 0; u < list_u.size(); u++) {
                if (list_u.get(u).getFg().equals(list_fg.get(i).getName())) {
                   user=new LCChatKitUser(
                           list_u.get(u).getUserId()
                           ,list_u.get(u).getUserName()
                           ,list_u.get(u).getAvatarUrl()
                           ,list_u.get(u).getFg()
                           ,list_u.get(u).getOwenr()
                   );
                    l.add(user);
                }
            }
            list_fg.get(i).setFriends(l);
        }

        adapter = new Contact_fg_Adapter(list_fg, getActivity());
        friendgroup_lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }
}
