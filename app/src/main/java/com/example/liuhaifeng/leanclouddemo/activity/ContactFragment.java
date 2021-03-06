package com.example.liuhaifeng.leanclouddemo.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.liuhaifeng.leanclouddemo.CustomUserProvider;
import com.example.liuhaifeng.leanclouddemo.MemberLetterEvent;
import com.example.liuhaifeng.leanclouddemo.MembersAdapter;
import com.example.liuhaifeng.leanclouddemo.R;

import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import de.greenrobot.event.EventBus;


public class ContactFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MembersAdapter itemAdapter;
    LinearLayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.contact_fragment_srl_list);
        recyclerView= (RecyclerView) view.findViewById(R.id.contact_fragment_rv_list);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
        itemAdapter = new MembersAdapter();
        recyclerView.setAdapter(itemAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMembers();
            }
        });

        EventBus.getDefault().register(this);
        return view;
    }
    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMembers();
        
    }
    private void refreshMembers() {
        itemAdapter.setMemberList(CustomUserProvider.getInstance().getAllUsers());
        itemAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 处理 LetterView 发送过来的 MemberLetterEvent
     * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
     */
    public void onEvent(MemberLetterEvent event) {
        Character targetChar = Character.toLowerCase(event.letter);
        if (itemAdapter.getIndexMap().containsKey(targetChar)) {
            int index = itemAdapter.getIndexMap().get(targetChar);
            if (index > 0 && index < itemAdapter.getItemCount()) {
                layoutManager.scrollToPositionWithOffset(index, 0);
            }
        }
    }

}