package com.yberry.dinehawaii.common.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackResponseData {

    @SerializedName("response_text")
    @Expose
    private String responseText;
    @SerializedName("response_by_user_id")
    @Expose
    private String responseByUserId;
    @SerializedName("response_by_user_name")
    @Expose
    private String responseByUserName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("reply_by")
    @Expose
    private String replyBy = "";

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getResponseByUserId() {
        return responseByUserId;
    }

    public void setResponseByUserId(String responseByUserId) {
        this.responseByUserId = responseByUserId;
    }

    public String getResponseByUserName() {
        return responseByUserName;
    }

    public void setResponseByUserName(String responseByUserName) {
        this.responseByUserName = responseByUserName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    @Override
    public String toString() {
        return "FeedbackResponseData{" +
                "responseText='" + responseText + '\'' +
                ", responseByUserId='" + responseByUserId + '\'' +
                ", responseByUserName='" + responseByUserName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", businessName='" + businessName + '\'' +
                ", businessId='" + businessId + '\'' +
                ", replyBy='" + replyBy + '\'' +
                '}';
    }
}
