package com.interrogation.result;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;

@Data
@ToString
public class ResponseResult {

    private Integer code;
    private String message;
    private HashMap<String,Object> data = new HashMap();

    private ResponseResult(){}

    public static ResponseResult success(){
        ResponseResult result = new ResponseResult();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(ResponseEnum.SUCCESS.getMessage());
        return result;
    }

    public static ResponseResult error(){
        ResponseResult result = new ResponseResult();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(ResponseEnum.ERROR.getMessage());
        return result;
    }

    public ResponseResult setCode(Integer code){
        this.code = code;
        return this;
    }

    public ResponseResult setMessage(String message){
        this.message = message;
        return this;
    }
    public ResponseResult setData(HashMap data){
        this.data = data;
        return this;
    }

    public static ResponseResult setResult(ResponseEnum responseEnum){
        ResponseResult result = new ResponseResult();
        result.setCode(responseEnum.getCode());
        result.setMessage(responseEnum.getMessage());
        return result;
    }
}
