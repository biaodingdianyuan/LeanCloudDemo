package com.example.liuhaifeng.leanclouddemo;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;

import java.io.Serializable;
import java.util.List;


/**
 * Created by liuhaifeng on 2017/1/7.
 */

public class ConversationHandler extends Service{
        SQLiteDatabase DB;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVIMMessageManager.setConversationEventHandler(new Handler());

    }

    class  Handler extends AVIMConversationEventHandler {
        @Override
        public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {



        }

        @Override
        public void onOfflineMessagesUnread(AVIMClient client, AVIMConversation conversation, int unreadCount) {

        }

        @Override
        public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {

        }

        @Override
        public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {

        }

        @Override
        public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {

        }

    }

}
