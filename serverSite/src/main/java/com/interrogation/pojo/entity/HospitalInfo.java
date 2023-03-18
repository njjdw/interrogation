package com.interrogation.pojo.entity;

import java.util.Date;

public class HospitalInfo extends BaseEntity{
    private Integer id;

    private String hosName;

    private String hosAddress;

    private String hosPhone;

    private Integer hosGrade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName == null ? null : hosName.trim();
    }

    public String getHosAddress() {
        return hosAddress;
    }

    public void setHosAddress(String hosAddress) {
        this.hosAddress = hosAddress == null ? null : hosAddress.trim();
    }

    public String getHosPhone() {
        return hosPhone;
    }

    public void setHosPhone(String hosPhone) {
        this.hosPhone = hosPhone == null ? null : hosPhone.trim();
    }

    public Integer getHosGrade() {
        return hosGrade;
    }

    public void setHosGrade(Integer hosGrade) {
        this.hosGrade = hosGrade;
    }

}