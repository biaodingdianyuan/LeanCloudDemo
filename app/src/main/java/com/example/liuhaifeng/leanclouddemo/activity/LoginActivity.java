package com.example.liuhaifeng.leanclouddemo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.liuhaifeng.leanclouddemo.ConversationHandler;
import com.example.liuhaifeng.leanclouddemo.MyleancloudAPP;
import com.example.liuhaifeng.leanclouddemo.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCIMData;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText name, password;
    private Button sign, register;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private SQLiteDatabase db;


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void init() {
        name = (EditText) findViewById(R.id.name);
       // password = (EditText) findViewById(R.id.password);
        sign = (Button) findViewById(R.id.sign);
     //   register = (Button) findViewById(R.id.register);
        //用户登录
        sign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {





                        LCChatKit.getInstance().open(name.getText().toString(), new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                                if (e == null) {

                                    MyleancloudAPP.name=name.getText().toString();
                                    LoginActivity.this.finish();
                                    LCIMData.name=name.getText().toString();
                                    List<LCChatKitUser> list_f=LCIMData.User_list;
                                    db=MyleancloudAPP.friendOpenHelper.getWritableDatabase();
                                    Cursor c=db.query("friend",null,"owenr=?",new String[]{name.getText().toString()},null,null,null);
                                    if(c.getCount()>0){

                                    }else{
                                        ContentValues v;
                                        for(int i=0;i<list_f.size();i++){
                                            v=new ContentValues();
                                            v.put("name",list_f.get(i).getUserName());
                                            v.put("avatarUrl",list_f.get(i).getAvatarUrl());
                                            v.put("fg",list_f.get(i).getFg());
                                            v.put("userId",list_f.get(i).getUserId());
                                            v.put("owenr",name.getText().toString());
                                            db.insert("friend",null,v);
                                        }
                                    }

                                    db=MyleancloudAPP.friendfroupOpenHelper.getWritableDatabase();
                                    Cursor cursor=db.query("friendgroup",null,"owenr=?",new String[]{name.getText().toString()},null,null,null);
                                    if(cursor.getCount()>0){

                                    }else{
                                        ContentValues values=new ContentValues();
                                        values.put("owenr",name.getText().toString());
                                        values.put("name","我的好友");
                                        db.insert("friendgroup",null,values);
                                    }

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Intent intent=new Intent(LoginActivity.this,ConversationHandler.class);
                                    startService(intent);

                                } else {

                                    Toast.makeText(LoginActivity.this, "登陆失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });







    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

