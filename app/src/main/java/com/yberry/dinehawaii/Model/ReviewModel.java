package com.yberry.dinehawaii.Model;

/**
 * Created by abc on 8/22/2017.
 */

public class ReviewModel {
    String review_question;
    String review_message;
    String time;
    String image;
    String food;
    String BussName;
    String resv_id;
    String date;
    String rating;
    String customer_id;
    String business_reply = "";
    String business_id;

    public String getBusiness_reply() {
        return business_reply;
    }

    public void setBusiness_reply(String business_reply) {
        this.business_reply = business_reply;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBussName() {
        return BussName;
    }

    public void setBussName(String bussName) {
        BussName = bussName;
    }

    public String getResv_id() {
        return resv_id;
    }

    public void setResv_id(String resv_id) {
        this.resv_id = resv_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }


    public String getReview_question() {
        return review_question;
    }

    public void setReview_question(String review_question) {
        this.review_question = review_question;
    }

    public String getReview_message() {
        return review_message;
    }

    public void setReview_message(String review_message) {
        this.review_message = review_message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }







   /* "review_question": "Test",
            "review_message": "Very good",
            "time": "2017-07-03 14:08:PM",
            "image": "http://www.truckslogistics.com/Projects-Work/Hawaii/web/front/images/rest_1.png"*/
}
