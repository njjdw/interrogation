package com.interrogation.controller.api;

import com.interrogation.exception.Assert;
import com.interrogation.pojo.dto.UserAccountDTO;
import com.interrogation.pojo.dto.UserInfoDTO;
import com.interrogation.result.ResponseEnum;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.UserInfoService;
import com.interrogation.utils.JwtUtils;
import com.interrogation.utils.RegexValidateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 1、注册信息是否为空，包括手机号、密码、验证码
     * 2、手机号、密码是否合法
     * 3、验证码是否正确
     * 4、注册(密码MD5加密)
     *
     * @return
     */
    @PostMapping("/regist")
    public ResponseResult regist(@RequestBody UserAccountDTO userAccountDTO){

        Assert.notNullOrEmpty(userAccountDTO.getUserPhone(), ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notNullOrEmpty(userAccountDTO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNullOrEmpty(userAccountDTO.getCheckCode(),ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(userAccountDTO.getUserPhone()),ResponseEnum.MOBILE_ERROR);
        Assert.isTrue(RegexValidateUtils.check(userAccountDTO.getPassword(),"^[a-zA-Z0-9]{6,9}$"),ResponseEnum.PASSWORD_INVALID);

        String code = (String)redisTemplate.opsForValue().get("interrogation:sms:code:" + userAccountDTO.getUserPhone());
        Assert.equals(userAccountDTO.getCheckCode(),code,ResponseEnum.CODE_ERROR);

        userInfoService.registUser(userAccountDTO.getUserPhone(), userAccountDTO.getPassword());
        return ResponseResult.success().setMessage("注册成功");
    }

    /**
     * 1、登录信息是否为空，包括手机号、密码、验证码
     * 2、验证码是否正确
     * 3、从数据库获取对应手机号的用户信息
     * 4、登陆密码做MD5加密
     * 5、检验密码是否正确
     * @param userAccountDTO 登录信息
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserAccountDTO userAccountDTO,
                                HttpServletRequest request){
        System.out.println(userAccountDTO);
        Assert.notNullOrEmpty(userAccountDTO.getUserPhone(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notNullOrEmpty(userAccountDTO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notNullOrEmpty(userAccountDTO.getCheckCode(),ResponseEnum.CODE_NULL_ERROR);

        String ip = request.getRemoteAddr();
        String checkCode = (String)redisTemplate.opsForValue().get("interrogation:img:code:" + ip);
        Assert.isTrue(userAccountDTO.getCheckCode().equals(checkCode),ResponseEnum.CODE_ERROR);
        UserInfoDTO loginUser = userInfoService.loginUser(userAccountDTO.getUserPhone(), userAccountDTO.getPassword());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("userInfo",loginUser);
        return ResponseResult.success().setMessage("登录成功").setData(map);
    }

    @GetMapping("/checkToken")
    public ResponseResult checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);
        if(result){
            return ResponseResult.success();
        }else{
            //LOGIN_AUTH_ERROR(-211, "未登录"),
            return ResponseResult.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }
}
