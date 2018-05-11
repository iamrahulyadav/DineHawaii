package com.yberry.dinehawaii.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationDetails {

    @SerializedName("setCheckedIn")
    @Expose
    private Integer setCheckedIn;
    @SerializedName("setWaitTime")
    @Expose
    private String setWaitTime;
    @SerializedName("setTableReady")
    @Expose
    private Integer setTableReady;
    @SerializedName("setSeatedBy")
    @Expose
    private String setSeatedBy;
    @SerializedName("setReschedule")
    @Expose
    private String setReschedule;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("party_size")
    @Expose
    private String partySize;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("reservation_id")
    @Expose
    private String reservationId;
    @SerializedName("table_number")
    @Expose
    private String tableNumber;
    @SerializedName("reserv_id")
    @Expose
    private String reservId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("checkin_time")
    @Expose
    private String checkinTime;
    @SerializedName("notice_Sent_time")
    @Expose
    private String noticeSentTime;
    @SerializedName("seating_time")
    @Expose
    private String seatingTime;
    @SerializedName("cancelation_time")
    @Expose
    private String cancelationTime;
    @SerializedName("reservation_status")
    @Expose
    private String reservationStatus;
    @SerializedName("reservation_booked_date")
    @Expose
    private String reservationBookedDate;

    @SerializedName("no_show_time")
    @Expose
    private String no_show_time;

    public String getReservation_amount() {
        return reservation_amount;
    }

    public void setReservation_amount(String reservation_amount) {
        this.reservation_amount = reservation_amount;
    }

    @SerializedName("reservation_amount")
    @Expose
    private String reservation_amount;

    public String getNo_show_time() {
        return no_show_time;
    }

    public void setNo_show_time(String no_show_time) {
        this.no_show_time = no_show_time;
    }

    public boolean isBlinked() {
        return isBlinked;
    }


    public void setBlinked(boolean blinked) {
        isBlinked = blinked;
    }

    boolean isBlinked=false;

    boolean isAlreadyBlinked=false;

    public Integer getSetCheckedIn() {
        return setCheckedIn;
    }

    public void setSetCheckedIn(Integer setCheckedIn) {
        this.setCheckedIn = setCheckedIn;
    }

    public String getSetWaitTime() {
        return setWaitTime;
    }

    public void setSetWaitTime(String setWaitTime) {
        this.setWaitTime = setWaitTime;
    }

    public Integer getSetTableReady() {
        return setTableReady;
    }

    public void setSetTableReady(Integer setTableReady) {
        this.setTableReady = setTableReady;
    }

    public String getSetSeatedBy() {
        return setSeatedBy;
    }

    public void setSetSeatedBy(String setSeatedBy) {
        this.setSeatedBy = setSeatedBy;
    }

    public String getSetReschedule() {
        return setReschedule;
    }

    public void setSetReschedule(String setReschedule) {
        this.setReschedule = setReschedule;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPartySize() {
        return partySize;
    }

    public void setPartySize(String partySize) {
        this.partySize = partySize;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getReservId() {
        return reservId;
    }

    public void setReservId(String reservId) {
        this.reservId = reservId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getNoticeSentTime() {
        return noticeSentTime;
    }

    public void setNoticeSentTime(String noticeSentTime) {
        this.noticeSentTime = noticeSentTime;
    }

    public String getSeatingTime() {
        return seatingTime;
    }

    public void setSeatingTime(String seatingTime) {
        this.seatingTime = seatingTime;
    }

    public String getCancelationTime() {
        return cancelationTime;
    }

    public void setCancelationTime(String cancelationTime) {
        this.cancelationTime = cancelationTime;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationBookedDate() {
        return reservationBookedDate;
    }

    public void setReservationBookedDate(String reservationBookedDate) {
        this.reservationBookedDate = reservationBookedDate;
    }

    @Override
    public String toString() {
        return "ReservationDetails{" +
                "setCheckedIn=" + setCheckedIn +
                ", setWaitTime='" + setWaitTime + '\'' +
                ", setTableReady=" + setTableReady +
                ", setSeatedBy='" + setSeatedBy + '\'' +
                ", setReschedule='" + setReschedule + '\'' +
                ", businessName='" + businessName + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", partySize='" + partySize + '\'' +
                ", emailId='" + emailId + '\'' +
                ", reservationId='" + reservationId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", reservId='" + reservId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", checkinTime='" + checkinTime + '\'' +
                ", noticeSentTime='" + noticeSentTime + '\'' +
                ", seatingTime='" + seatingTime + '\'' +
                ", cancelationTime='" + cancelationTime + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", reservationBookedDate='" + reservationBookedDate + '\'' +
                '}';
    }
}