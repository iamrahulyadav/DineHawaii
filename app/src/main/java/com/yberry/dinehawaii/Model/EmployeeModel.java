package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abc on 3/10/2018.
 */

public class EmployeeModel implements Parcelable {
    public static final Creator<EmployeeModel> CREATOR = new Creator<EmployeeModel>() {
        @Override
        public EmployeeModel createFromParcel(Parcel in) {
            return new EmployeeModel(in);
        }

        @Override
        public EmployeeModel[] newArray(int size) {
            return new EmployeeModel[size];
        }
    };

    public String getEmp_firstnm() {
        return emp_firstnm;
    }

    public void setEmp_firstnm(String emp_firstnm) {
        this.emp_firstnm = emp_firstnm;
    }

    public String getEmp_jobtitle() {
        return emp_jobtitle;
    }

    public void setEmp_jobtitle(String emp_jobtitle) {
        this.emp_jobtitle = emp_jobtitle;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_dineid() {
        return emp_dineid;
    }

    public void setEmp_dineid(String emp_dineid) {
        this.emp_dineid = emp_dineid;
    }

    public String getEmp_pass() {
        return emp_pass;
    }

    public void setEmp_pass(String emp_pass) {
        this.emp_pass = emp_pass;
    }

    public String getEmp_phoneno() {
        return emp_phoneno;
    }

    public void setEmp_phoneno(String emp_phoneno) {
        this.emp_phoneno = emp_phoneno;
    }

    public EmployeeModel() {

    }

    String emp_firstnm;
    String emp_jobtitle;
    String emp_email;
    String emp_dineid;
    String emp_pass;
    String emp_phoneno;
    String emp_jobid;
    String emp_lastmn;
    String jobduty;
    String duties;
    String emp_id;
    String status ;

    public EmployeeModel(Parcel in) {
        emp_firstnm = in.readString();
        emp_jobtitle = in.readString();
        emp_email = in.readString();
        emp_dineid = in.readString();
        emp_pass = in.readString();
        emp_phoneno = in.readString();
        emp_lastmn = in.readString();
        jobduty = in.readString();
        emp_jobid = in.readString();
        duties = in.readString();
        emp_id = in.readString();
        status  = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public String toString() {
        return "EmployeeModel{" +
                "emp_id='" + emp_id + '\'' +
                "emp_firstnm='" + emp_firstnm + '\'' +
                ", emp_jobtitle='" + emp_jobtitle + '\'' +
                ", emp_email='" + emp_email + '\'' +
                ", emp_pass='" + emp_pass + '\'' +
                ", emp_dineid='" + emp_dineid + '\'' +
                ", emp_phoneno='" + emp_phoneno + '\'' +
                ", emp_jobid='" + emp_jobid + '\'' +
                ", emp_lastmn='" + emp_lastmn + '\'' +
                ", jobduty='" + jobduty + '\'' +
                ", duties='" + duties + '\'' +
                ", status ='" + status  + '\'' +
                '}';
    }

    public String getEmp_jobid() {
        return emp_jobid;
    }

    public void setEmp_jobid(String emp_jobid) {
        this.emp_jobid = emp_jobid;
    }

    public String getEmp_lastmn() {
        return emp_lastmn;
    }

    public void setEmp_lastmn(String emp_lastmn) {
        this.emp_lastmn = emp_lastmn;
    }

    public String getJobduty() {
        return jobduty;
    }

    public void setJobduty(String jobduty) {
        this.jobduty = jobduty;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(emp_id);
        parcel.writeString(emp_firstnm);
        parcel.writeString(emp_jobtitle);
        parcel.writeString(emp_dineid);
        parcel.writeString(emp_email);
        parcel.writeString(emp_phoneno);
        parcel.writeString(emp_pass);
        parcel.writeString(emp_jobid);
        parcel.writeString(emp_lastmn);
        parcel.writeString(jobduty);
        parcel.writeString(duties);
        parcel.writeString(status );
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
