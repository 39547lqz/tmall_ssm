package com.lqz.tmall_ssm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Review {
    private Integer id;

    private String content;

    private Integer uid;

    private Integer pid;

    private Date createDate;

    /*非数据库字段*/
    @Setter
    @Getter
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}