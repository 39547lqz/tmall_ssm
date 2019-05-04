package com.lqz.tmall_ssm.pojo;

import lombok.Getter;
import lombok.Setter;

public class Property {
    private Integer id;

    private Integer cid;

    private String name;

    /*非数据库字段*/
    @Getter
    @Setter
    private Category  category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}