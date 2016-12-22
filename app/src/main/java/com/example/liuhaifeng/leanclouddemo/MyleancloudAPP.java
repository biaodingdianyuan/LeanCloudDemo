package com.example.liuhaifeng.leanclouddemo;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.adapter.LCIMChatAdapter;

/**
 * Created by liuhaifeng on 2016/12/12.
 */

public class MyleancloudAPP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());

        LCChatKit.getInstance().init(getApplicationContext(), "kayb5IFFY6i1wVVfv2T16w0c-gzGzoHsz", "CQgEbRumu3bhK89unzdGnbim");

        AVOSCloud.setDebugLogEnabled(true);



    }


}
