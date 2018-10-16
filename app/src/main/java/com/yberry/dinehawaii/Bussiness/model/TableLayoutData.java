package com.yberry.dinehawaii.Bussiness.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TableLayoutData {

    @SerializedName("table_id")
    @Expose
    private String tableId;
    @SerializedName("reservation_id")
    @Expose
    private String reservationId;
    @SerializedName("table_size")
    @Expose
    private String tableSize;
    @SerializedName("table_name")
    @Expose
    private String tableName;
    @SerializedName("seating_time")
    @Expose
    private String seatingTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("booking_time")
    @Expose
    private String bookingTime;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTableSize() {
        return tableSize;
    }

    public void setTableSize(String tableSize) {
        this.tableSize = tableSize;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSeatingTime() {
        return seatingTime;
    }

    public void setSeatingTime(String seatingTime) {
        this.seatingTime = seatingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

}