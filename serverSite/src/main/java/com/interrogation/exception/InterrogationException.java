package com.interrogation.exception;

import com.interrogation.result.ResponseEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterrogationException extends RuntimeException{
    private Integer code;
    private String message;

    public InterrogationException(Integer code){
        this.code = code;
    }

    public InterrogationException(String message){
        this.message = message;
    }

    public InterrogationException(ResponseEnum responseEnum){
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public InterrogationException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public InterrogationException(ResponseEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }
}
