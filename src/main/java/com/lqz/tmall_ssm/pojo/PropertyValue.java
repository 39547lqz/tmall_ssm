package com.lqz.tmall_ssm.pojo;

import lombok.Getter;
import lombok.Setter;

public class PropertyValue {
    private Integer id;

    private Integer pid;

    private Integer ptid;

    private String value;

    /*非数据库字段*/
    @Setter
    @Getter
    private Property property;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getPtid() {
        return ptid;
    }

    public void setPtid(Integer ptid) {
        this.ptid = ptid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}