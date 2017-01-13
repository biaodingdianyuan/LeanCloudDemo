package cn.leancloud.chatkit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;

import java.util.Arrays;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCIMData;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.event.LCIMConversationItemLongClickEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

/**
 * Created by wli on 16/2/29.
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 */
public class LCIMConversationActivity extends AppCompatActivity {

  protected LCIMConversationFragment conversationFragment;
  private TextView title_n;
  private ImageView back,add;
  private Toolbar toolbar;
  Bundle extras;
  String finalConversationId;
  private AlertDialog alert = null;
  private AlertDialog.Builder builder = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getSupportActionBar().hide();
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.lcim_conversation_activity);
    conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
    initByIntent(getIntent());


  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    initByIntent(intent);
  }

  private void initByIntent(Intent intent) {
    if (null == LCChatKit.getInstance().getClient()) {
      showToast("please login first!");
      finish();
      return;
    }

    extras= intent.getExtras();
    if (null != extras) {
      if (extras.containsKey(LCIMConstants.PEER_ID)) {
        getConversation(extras.getString(LCIMConstants.PEER_ID));
      } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
        String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
        updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
      } else {
        showToast("memberId or conversationId is needed");
        finish();
      }
    }
  }

  /**
   * 设置 actionBar title 以及 up 按钮事件
   *
   * @param title
   */
  protected void initActionBar(String title) {
    title_n=(TextView)findViewById(R.id.lcname);
    back= (ImageView) findViewById(R.id.back_lc);
    toolbar= (Toolbar) findViewById(R.id.toolbar);
    add= (ImageView) findViewById(R.id.add);
    title_n.setText(title);
    String conversationId=null;
    add.setVisibility(View.VISIBLE);

    if (null != extras) {
      if (extras.containsKey(LCIMConstants.PEER_ID)) {

      } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
        conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
        add.setVisibility(View.VISIBLE);


      } else {
        showToast("memberId or conversationId is needed");
        finish();
      }
    }

    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    finalConversationId = conversationId;
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       InpopupWincow(v);
      }
    });


  }

  public  void InpopupWincow(View v){
    View view = getLayoutInflater().inflate(R.layout.popupwindow, null);
    TextView add_group = (TextView) view.findViewById(R.id.addtogroup);
    TextView out_group = (TextView) view.findViewById(R.id.outgroup);

    final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

    popupWindow.setTouchable(true);
    popupWindow.setTouchInterceptor(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return false;
        // 这里如果返回true的话，touch事件将被拦截
        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
      }
    });
    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


    //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
    popupWindow.showAsDropDown(v,  toolbar.getWidth()- view.getWidth(), toolbar.getHeight() );
    //邀请好友
       add_group.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LCIMConversationActivity.this,AddfriendtogroupActivity.class).putExtra("id", finalConversationId));
        popupWindow.dismiss();

      }
    });
    //退出群组
    out_group.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alert = null;
        builder = new AlertDialog.Builder(LCIMConversationActivity.this);
        alert = builder
                .setTitle("提示：")
                .setMessage("您确定要退出该群组吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {


                  }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    AVIMClient tom = AVIMClient.getInstance(LCIMData.name);
                    tom.open(new AVIMClientCallback(){

                      @Override
                      public void done(AVIMClient client,AVIMException e){
                        if(e==null){
                          //登录成功
                          final AVIMConversation conv = client.getConversation(finalConversationId);
                          conv.join(new AVIMConversationCallback(){
                            @Override
                            public void done(AVIMException e){
                              if(e==null){
                                //加入成功
                                conv.quit(new AVIMConversationCallback(){
                                  @Override
                                  public void done(AVIMException e){
                                    if(e==null){
                                   Toast.makeText(LCIMConversationActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                                 LCIMConversationListFragment.onEvent(new LCIMConversationItemLongClickEvent(conv));
                                    finish();
                                    }
                                  }
                                });
                              }
                            }
                          });
                        }
                      }
                    });

                  }
                }).create();
        alert.show();
        popupWindow.dismiss();
      }
    });



  }




  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (android.R.id.home == item.getItemId()) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 主动刷新 UI
   *
   * @param conversation
   */
  protected void updateConversation(AVIMConversation conversation) {
    if (null != conversation) {
      conversationFragment.setConversation(conversation);
      LCIMConversationItemCache.getInstance().clearUnread(conversation.getConversationId());
      LCIMConversationUtils.getConversationName(conversation, new AVCallback<String>() {
        @Override
        protected void internalDone0(String s, AVException e) {
          if (null != e) {
            LCIMLogUtils.logException(e);
          } else {
            initActionBar(s);
          }
        }
      });
    }
  }

  /**
   * 获取 conversation
   * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
   */
  protected void getConversation(final String memberId) {
    LCChatKit.getInstance().getClient().createConversation(
      Arrays.asList(memberId), "", null, false, true, new AVIMConversationCreatedCallback() {
        @Override
        public void done(AVIMConversation avimConversation, AVIMException e) {
          if (null != e) {
            showToast(e.getMessage());
          } else {
            updateConversation(avimConversation);
          }
        }
      });
  }

  /**
   * 弹出 toast
   *
   * @param content
   */
  private void showToast(String content) {
    Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
  }
}