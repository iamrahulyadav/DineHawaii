package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hvantage3 on 7/4/2017.
 */

public class SelectedCustomizationModel implements Parcelable {
    public SelectedCustomizationModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        isSelectedCheck = in.readByte() != 0;
    }

    public static final Creator<SelectedCustomizationModel> CREATOR = new Creator<SelectedCustomizationModel>() {
        @Override
        public SelectedCustomizationModel createFromParcel(Parcel in) {
            return new SelectedCustomizationModel(in);
        }

        @Override
        public SelectedCustomizationModel[] newArray(int size) {
            return new SelectedCustomizationModel[size];
        }
    };

    public SelectedCustomizationModel() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelectedCheck(boolean selectedCheck) {
        isSelectedCheck = selectedCheck;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelectedCheck() {
        return isSelectedCheck;
    }

    private String id;
    private String name;
    private boolean isSelectedCheck;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isSelectedCheck ? 1 : 0));
    }
}
