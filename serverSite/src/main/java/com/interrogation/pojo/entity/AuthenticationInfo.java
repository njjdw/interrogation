package com.interrogation.pojo.entity;

import lombok.ToString;

@ToString
public class AuthenticationInfo extends BaseEntity{
    private Integer id;

    private Integer userId;

    private String userName;

    private String idCard;

    private Integer hospitalId;

    private Integer department;

    private Integer level;

    private String qualification;

    private Integer authenticateStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification == null ? null : qualification.trim();
    }

    public Integer getAuthenticateStatus() {
        return authenticateStatus;
    }

    public void setAuthenticateStatus(Integer authenticateStatus) {
        this.authenticateStatus = authenticateStatus;
    }

}