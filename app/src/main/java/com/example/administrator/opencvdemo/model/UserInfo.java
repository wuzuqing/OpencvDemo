package com.example.administrator.opencvdemo.model;



/**
 * @author 吴祖清
 * @version 1.0
 * @createDate 2018/1/1 21:51
 * @des ${TODO}
 * @updateAuthor #author
 * @updateDate 2018/1/1
 * @updateDes ${TODO}
 */

public class UserInfo {
    private Long id;

    private String name;
    private String pwd;

    private long lastRefreshZhengWuTime ;   //政务
    private long lastRefreshShouCaiTime ;   //书院
    private long lastRefreshFengLuTime ;    //俸禄
    private long lastRefreshMoBaiTime ; //膜拜
    private long lastRefreshXunFangTime ;//寻访
    private long lastRefreshSuijiTime ; // 随机
    private long lastRefreshTaskTime ;//任务

    
    public UserInfo(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public UserInfo(Long id, String name, String pwd, long lastRefreshZhengWuTime,
            long lastRefreshShouCaiTime, long lastRefreshFengLuTime,
            long lastRefreshMoBaiTime, long lastRefreshXunFangTime,
            long lastRefreshSuijiTime, long lastRefreshTaskTime) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.lastRefreshZhengWuTime = lastRefreshZhengWuTime;
        this.lastRefreshShouCaiTime = lastRefreshShouCaiTime;
        this.lastRefreshFengLuTime = lastRefreshFengLuTime;
        this.lastRefreshMoBaiTime = lastRefreshMoBaiTime;
        this.lastRefreshXunFangTime = lastRefreshXunFangTime;
        this.lastRefreshSuijiTime = lastRefreshSuijiTime;
        this.lastRefreshTaskTime = lastRefreshTaskTime;
    }

    public UserInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public long getLastRefreshZhengWuTime() {
        return lastRefreshZhengWuTime;
    }

    public void setLastRefreshZhengWuTime(long lastRefreshZhengWuTime) {
        this.lastRefreshZhengWuTime = lastRefreshZhengWuTime;
    }

    public long getLastRefreshShouCaiTime() {
        return lastRefreshShouCaiTime;
    }

    public void setLastRefreshShouCaiTime(long lastRefreshShouCaiTime) {
        this.lastRefreshShouCaiTime = lastRefreshShouCaiTime;
    }

    public long getLastRefreshFengLuTime() {
        return lastRefreshFengLuTime;
    }

    public void setLastRefreshFengLuTime(long lastRefreshFengLuTime) {
        this.lastRefreshFengLuTime = lastRefreshFengLuTime;
    }

    public long getLastRefreshMoBaiTime() {
        return lastRefreshMoBaiTime;
    }

    public void setLastRefreshMoBaiTime(long lastRefreshMoBaiTime) {
        this.lastRefreshMoBaiTime = lastRefreshMoBaiTime;
    }

    public long getLastRefreshXunFangTime() {
        return lastRefreshXunFangTime;
    }

    public void setLastRefreshXunFangTime(long lastRefreshXunFangTime) {
        this.lastRefreshXunFangTime = lastRefreshXunFangTime;
    }

    public long getLastRefreshSuijiTime() {
        return lastRefreshSuijiTime;
    }

    public void setLastRefreshSuijiTime(long lastRefreshSuijiTime) {
        this.lastRefreshSuijiTime = lastRefreshSuijiTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLastRefreshTaskTime() {
        return this.lastRefreshTaskTime;
    }

    public void setLastRefreshTaskTime(long lastRefreshTaskTime) {
        this.lastRefreshTaskTime = lastRefreshTaskTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "id=" + id + ", name='" + name + '\'' + ", pwd='" + pwd + '}';
    }
}
