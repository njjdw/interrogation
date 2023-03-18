package com.interrogation.pojo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserAccountDTO {

    private Integer ip;
    private String userPhone;
    private String password;
    private String checkCode;
    private String salt;
}
