package com.yberry.dinehawaii.Bussiness.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessAreaModel {

    @SerializedName("area_id")
    @Expose
    private String areaId;
    @SerializedName("area_name")
    @Expose
    private String areaName;


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }


}
