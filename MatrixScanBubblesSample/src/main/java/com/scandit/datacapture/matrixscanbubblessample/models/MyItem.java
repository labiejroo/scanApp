package com.scandit.datacapture.matrixscanbubblessample.models;

public class MyItem {

    public MyItem(int id ,String name,String isScrap,String code,String remark)
    {
        this.id = id;
        this.code = code;
        this.isScrap = isScrap;
        this.name = name;
        this.remark = remark;
    };

    public int id;
    public String name;
    public String isScrap;
    public String code;
    public String remark;


    public MyItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIsScrap() {
        return isScrap;
    }

    public void setIsScrap(String isScrap) {
        this.isScrap = isScrap;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getCodeNumber() {
        return code;
    }

    public void setCodeNumber(String code) {
        this.code = code;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
