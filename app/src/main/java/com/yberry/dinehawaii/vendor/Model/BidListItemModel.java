package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidListItemModel {

    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("bid_status")
    @Expose
    private String bidStatus;

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

}
