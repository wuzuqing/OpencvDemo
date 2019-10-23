package com.example.administrator.opencvdemo.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/22 13:57
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/22$
 * @updateDes ${TODO}
 */

public class TaskModel {

    private String name;
    private long lastRefreshTime ;
    private int spaceTime;
    private List<PointModel> data;
    private int type;
    private Map<String,PointModel> mapData;
    private int count;

    public TaskModel() {
    }

    public TaskModel(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public Map<String, PointModel> getMapData() {
        return mapData;
    }

    public void setMapData(Map<String, PointModel> mapData) {
        this.mapData = mapData;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public int getSpaceTime() {
        return spaceTime;
    }

    public void setSpaceTime(int spaceTime) {
        this.spaceTime = spaceTime;
    }

    public  List<PointModel>  getData() {
        return data;
    }

    public void setData( List<PointModel>  data) {
        this.data = data;
        mapData = change();
    }

    private Map<String, PointModel> change() {
        Map<String, PointModel> result = new TreeMap<>();
        for (PointModel pointModel : this.data) {
            result.put(pointModel.getKey(),pointModel);
        }
        return result;
    }

    @Override
    public String toString() {
        return "TaskModel{" + "name='" + name + '\'' + ", lastRefreshTime=" + lastRefreshTime + ", spaceTime=" + spaceTime + ", data=" +
                data + ", type=" + type + '}';
    }
}
