package cn.leancloud.chatkit;

import com.avos.avoscloud.AVObject;

/**
 * Created by wli on 16/2/2.
 * LCChatKit 中的用户类，仅包含三个变量，暂不支持继承扩展
 */
public final class LCChatKitUser extends AVObject {
  private String userId;
  private String avatarUrl;
  private String name;
  private String fg;
  private String owenr;

  public LCChatKitUser(String userId, String userName, String avatarUrl,String fg,String owenr) {
    this.userId = userId;
    this.avatarUrl = avatarUrl;
    this.name = userName;
    this.fg=fg;
    this.owenr=owenr;
  }

  public String getUserId() {
    return userId;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getUserName() {
    return name;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFg() {
    return fg;
  }

  public void setFg(String fg) {
    this.fg = fg;
  }

  public String getOwenr() {
    return owenr;
  }

  public void setOwenr(String owenr) {
    this.owenr = owenr;
  }
}
