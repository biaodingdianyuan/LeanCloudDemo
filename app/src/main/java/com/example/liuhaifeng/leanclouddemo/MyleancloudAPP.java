package com.example.liuhaifeng.leanclouddemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
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
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.activity.AddfriendtogroupActivity;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.adapter.LCIMChatAdapter;

/**
 * Created by liuhaifeng on 2016/12/12.
 */

public class MyleancloudAPP extends Application {

    public  static Context context;
    public static  String name;


    @Override
    public void onCreate() {
        super.onCreate();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        // TODO: 2017/1/7  添加 APPID，APPKEY
        LCChatKit.getInstance().init(getApplicationContext(),
                "----APPID----", "----APPKEY---");
        AVOSCloud.setDebugLogEnabled(true);
        context=getApplicationContext();
        //向依赖库中传值   测试数据
        LCIMData.User_list=CustomUserProvider.getInstance().getAllUsers();


    }


}
