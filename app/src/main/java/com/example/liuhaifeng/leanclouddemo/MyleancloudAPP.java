package com.example.liuhaifeng.leanclouddemo;
import android.app.Application;
import android.content.Context;
import com.avos.avoscloud.AVOSCloud;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCIMData;
public class MyleancloudAPP extends Application {

    public  static Context context;
    public static  String name;
    public  static  FriendfroupOpenHelper friendfroupOpenHelper;
    public  static  FriendOpenHelper friendOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        // TODO: 2017/1/7  添加 APPID，APPKEY
        LCChatKit.getInstance().init(getApplicationContext(),"kayb5IFFY6i1wVVfv2T16w0c-gzGzoHsz","CQgEbRumu3bhK89unzdGnbim");
        AVOSCloud.setDebugLogEnabled(true);
        context=getApplicationContext();
        //向依赖库中传值   测试数据
        LCIMData.User_list=CustomUserProvider.getInstance().getAllUsers();
        friendfroupOpenHelper=new FriendfroupOpenHelper(getApplicationContext(),"fg",null,1);
        friendOpenHelper=new FriendOpenHelper(getApplicationContext(),"friend",null,1);
    }


}
