package com.yberry.dinehawaii.common.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackData {
    @SerializedName("feedback_id")
    @Expose
    private String feedbackId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("response")
    @Expose
    private List<FeedbackResponseData> response = null;


    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FeedbackResponseData> getResponse() {
        return response;
    }

    public void setResponse(List<FeedbackResponseData> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "FeedbackData{" +
                "feedbackId='" + feedbackId + '\'' +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", reviewMessage='" + reviewMessage + '\'' +
                ", orderId='" + orderId + '\'' +
                ", type='" + type + '\'' +
                ", response=" + response +
                '}';
    }
}
