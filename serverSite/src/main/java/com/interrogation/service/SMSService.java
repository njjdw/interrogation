package com.interrogation.service;

import java.util.Map;

public interface SMSService {

    public void sendCode(String phone, Map<String,Object> paramMap);
}
