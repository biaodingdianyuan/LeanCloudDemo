package cn.leancloud.chatkit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;

import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.groupDao;


/**
 * Created by liuhaifeng on 2017/1/10.
 */
public class ItemActivity extends AppCompatActivity {

    private TextView name, id, creator, date;
    private groupDao list;
    private Button joinin;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        list = new groupDao();

        list = (groupDao) intent.getSerializableExtra("group");
        init();
    }

    private void init() {
        name = (TextView) findViewById(R.id.name_item);
        id = (TextView) findViewById(R.id.id_item);
        date = (TextView) findViewById(R.id.creatordat_item);
        creator = (TextView) findViewById(R.id.creato_item);
        joinin = (Button) findViewById(R.id.joinin_item);
        back= (ImageView) findViewById(R.id.back_item);
        name.setText(list.getName());
        id.setText(list.getId());
        date.setText(list.getDe());
        creator.setText(list.getOwenr());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        joinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AVIMClient tom = AVIMClient.getInstance(LCIMData.name);

                tom.open(new AVIMClientCallback() {

                    @Override
                    public void done(AVIMClient client, AVIMException e) {
                        if (e == null) {
                            //登录成功
                            final AVIMConversation conv = client.getConversation(id.getText().toString());
                            conv.join(new AVIMConversationCallback() {
                                @Override
                                public void done(AVIMException e) {
                                    if (e == null) {
                                        //加入成功
                                        Toast.makeText(ItemActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ItemActivity.this, "加入失败" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ItemActivity.this, "加入失败" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }
}
