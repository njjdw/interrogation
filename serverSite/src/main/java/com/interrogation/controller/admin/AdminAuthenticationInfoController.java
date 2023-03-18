package com.interrogation.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interrogation.pojo.entity.AuthenticationInfo;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.AuthenticationInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/authenticate")
public class AdminAuthenticationInfoController {

    @Resource
    private AuthenticationInfoService authenticationInfoService;

    @GetMapping("/getAllInfo/{pageNo}")
    public ResponseResult listInfo(@PathVariable("pageNo") Integer pageNo){
        PageHelper.startPage(pageNo,6);
        List<AuthenticationInfo> page = authenticationInfoService.getAllInfoByPage();
        PageInfo<AuthenticationInfo> pageInfo = new PageInfo<>(page);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageInfo",pageInfo);
        return ResponseResult.success().setData(resultMap);
    }

    @GetMapping("/status/{id}/{status}")
    public ResponseResult dealWithStatus(@PathVariable("id") Integer id,
                                         @PathVariable("status") Integer status){
        authenticationInfoService.changeStatus(id,status);
        return ResponseResult.success();
    }
}
