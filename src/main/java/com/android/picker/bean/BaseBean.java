package com.android.picker.bean;

/**
 * @author hanxw
 * @time 2019/7/4 14:05
 */
public class BaseBean {
    /**
     * 110101
     */
    public String id;
    /**
     * 东城区
     */
    public String name;



    @Override
    public String toString() {
        return "BaseBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public String getName() {
        return name == null ? "" : name.trim();
    }


}
