package com.example.liuhaifeng.leanclouddemo;

import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;

/**
 * Created by liuhaifeng on 2017/1/20.
 */

public class FriendGroupDao {
    private String name;
    private List<LCChatKitUser> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LCChatKitUser> getFriends() {
        return friends;
    }

    public void setFriends(List<LCChatKitUser> friends) {
        this.friends = friends;
    }
}
