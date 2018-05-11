package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abc on 3/7/2018.
 */

public class LoyaltyPointModel implements Parcelable {
    public String getBussName() {
        return bussName;
    }

    public void setBussName(String bussName) {
        this.bussName = bussName;
    }

    public String getBussId() {
        return bussId;
    }

    public void setBussId(String bussId) {
        this.bussId = bussId;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String bussName;
    String bussId;
    String totalPoints;
    String date;
    public static final Creator<LoyaltyPointModel> CREATOR = new Creator<LoyaltyPointModel>() {
        @Override
        public LoyaltyPointModel createFromParcel(Parcel in) {
            return new LoyaltyPointModel(in);
        }

        @Override
        public LoyaltyPointModel[] newArray(int size) {
            return new LoyaltyPointModel[size];
        }
    };

    public LoyaltyPointModel(Parcel in) {
        bussName = in.readString();
        bussId = in.readString();
        totalPoints = in.readString();
        date = in.readString();
    }

    public LoyaltyPointModel() {

    }

    @Override
    public String toString() {
        return "LoyaltyPointModel{" +
                ", bussName ='" + bussName + '\'' +
                ", bussId ='" + bussId + '\'' +
                ", totalPoints ='" + totalPoints + '\'' +
                ", date ='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bussName);
        dest.writeString(bussId);
        dest.writeString(totalPoints);
        dest.writeString(date);
    }
}
