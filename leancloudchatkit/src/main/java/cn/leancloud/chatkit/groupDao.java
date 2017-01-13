package cn.leancloud.chatkit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuhaifeng on 2016/12/21.
 */

public class groupDao implements Serializable {




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private String num;
    private List<String> member;
    private String owenr;

    public String getOwenr() {
        return owenr;
    }

    public void setOwenr(String owenr) {
        this.owenr = owenr;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    private  String de;


    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
