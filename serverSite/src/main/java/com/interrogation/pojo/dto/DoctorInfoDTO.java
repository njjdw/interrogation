package com.interrogation.pojo.dto;

import lombok.Data;

@Data
public class DoctorInfoDTO {

    private Integer userId;
    private String doctorName;
    private String hospital;
    private String department;
    private String level;
    private String qualification;
    private Integer authenticateStatus;
}
