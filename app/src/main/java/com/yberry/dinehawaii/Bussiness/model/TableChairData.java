package com.yberry.dinehawaii.Bussiness.model;

/**
 * Created by Hvantage on 2/26/2018.
 */

public class TableChairData
{
    int s_no=0;
    String chair_type="";
    String chair_desc="";
    boolean isSaved=false;

    public TableChairData(int s_no, String chair_type, String chair_desc, boolean isSaved) {
        this.s_no=s_no;
        this.chair_type=chair_type;
        this.chair_desc=chair_desc;
        this.isSaved=isSaved;

    }

    public int getS_no() {
        return s_no;
    }

    public void setS_no(int s_no) {
        this.s_no = s_no;
    }

    public String getChair_type() {
        return chair_type;
    }

    public void setChair_type(String chair_type) {
        this.chair_type = chair_type;
    }

    public String getChair_desc() {
        return chair_desc;
    }

    public void setChair_desc(String chair_desc) {
        this.chair_desc = chair_desc;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "TableChairData{" +
                "s_no=" + s_no +
                ", chair_type='" + chair_type + '\'' +
                ", chair_desc='" + chair_desc + '\'' +
                ", isSaved='" + isSaved + '\'' +
                '}';
    }
}
