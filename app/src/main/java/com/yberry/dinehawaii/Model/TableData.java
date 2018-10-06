package com.yberry.dinehawaii.Model;

/**
 * Created by hvantage3 on 7/10/2017.
 */


import java.io.Serializable;

/**
 * Created by RK on 08/07/2017.
 */

public class TableData implements Serializable {
    String table_id;
    String table_name;
    String table_capacity;
    String reserv_priority;
    String alot_hours;
    String alot_min;
    String table_descp;
    String chair_descp;
    String chair_type;
    String service_id;
    String tableDetails;
    String combineStatus = "";
    String isHandicapped;

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    boolean isSeleted = false;

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    String table_type = "";

    public String getTable_ids() {
        return table_ids;
    }

    public void setTable_ids(String table_ids) {
        this.table_ids = table_ids;
    }

    String table_ids;

    @Override
    public String toString() {
        return "TableData{" +
                "table_id='" + table_id + '\'' +
                ", table_name='" + table_name + '\'' +
                ", table_capacity='" + table_capacity + '\'' +
                ", reserv_priority='" + reserv_priority + '\'' +
                ", alot_hours='" + alot_hours + '\'' +
                ", alot_min='" + alot_min + '\'' +
                ", table_descp='" + table_descp + '\'' +
                ", chair_descp='" + chair_descp + '\'' +
                ", chair_type='" + chair_type + '\'' +
                ", service_id='" + service_id + '\'' +
                ", tableDetails='" + tableDetails + '\'' +
                ", combineStatus='" + combineStatus + '\'' +
                ", isHandicapped='" + isHandicapped + '\'' +
                ", service_name='" + service_name + '\'' +
                ", table_type='" + table_type + '\'' +
                ", table_ids='" + table_ids + '\'' +
                '}';
    }

    public String getCombineStatus() {
        return combineStatus;
    }

    public void setCombineStatus(String combineStatus) {
        this.combineStatus = combineStatus;
    }

    public String getIsHandicapped() {
        return isHandicapped;
    }

    public void setIsHandicapped(String isHandicapped) {
        this.isHandicapped = isHandicapped;
    }

    public String getReserv_priority() {
        return reserv_priority;
    }

    public void setReserv_priority(String reserv_priority) {
        this.reserv_priority = reserv_priority;
    }

    public String getAlot_hours() {
        return alot_hours;
    }

    public void setAlot_hours(String alot_hours) {
        this.alot_hours = alot_hours;
    }

    public String getAlot_min() {
        return alot_min;
    }

    public void setAlot_min(String alot_min) {
        this.alot_min = alot_min;
    }

    public String getTable_descp() {
        return table_descp;
    }

    public void setTable_descp(String table_descp) {
        this.table_descp = table_descp;
    }

    public String getChair_descp() {
        return chair_descp;
    }

    public void setChair_descp(String chair_descp) {
        this.chair_descp = chair_descp;
    }

    public String getChair_type() {
        return chair_type;
    }

    public void setChair_type(String chair_type) {
        this.chair_type = chair_type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    String service_name;

    public TableData() {
    }

    public TableData(String table_id, String table_name, String table_capacity) {
        this.table_id = table_id;
        this.table_name = table_name;
        this.table_capacity = table_capacity;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_capacity() {
        return table_capacity;
    }

    public void setTable_capacity(String table_capacity) {
        this.table_capacity = table_capacity;
    }

    public String getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(String tableDetails) {
        this.tableDetails = tableDetails;
    }
}
