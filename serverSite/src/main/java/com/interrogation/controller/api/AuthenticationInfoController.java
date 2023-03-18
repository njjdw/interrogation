package com.interrogation.controller.api;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interrogation.exception.InterrogationException;
import com.interrogation.pojo.dto.DoctorInfoDTO;
import com.interrogation.pojo.entity.AuthenticationInfo;
import com.interrogation.result.ResponseEnum;
import com.interrogation.result.ResponseResult;
import com.interrogation.service.AuthenticationInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/doctor")
public class AuthenticationInfoController {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AuthenticationInfoService authenticationInfoService;

    @GetMapping("/doctorList/{currentPage}")
    public ResponseResult getDoctors(@PathVariable("currentPage") Integer currentPage){
        PageHelper.startPage(currentPage+1,8);
        List<DoctorInfoDTO> allDoctors = authenticationInfoService.getAllDoctors();
        PageInfo<DoctorInfoDTO> listPageInfo = new PageInfo<>(allDoctors, currentPage + 1);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("doctors",listPageInfo);
        return ResponseResult.success().setData(map);
    }

    /**
     * 1、给图片起别名
     * 2、将图片存入文档保存
     * 3、图片位置存入数据库
     * 4、返回图片位置
     * @param qualification
     * @return
     */
    @PostMapping("/uploadQualification")
    public ResponseResult uploadQualification(@RequestParam("file") MultipartFile qualification,
                                              HttpServletRequest request){
        /**
         * 1、获取图片格式
         * 2、生成随机图片名
         * 3、获取存放图片的磁盘路径
         * 4、生成文件绝对路径
         * 5、生成文件对象
         * 6、传输文件
         * 7、响应传输图片的http路径
         */
        String filename = qualification.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        String baseName = UUID.randomUUID() +suffix;
        //磁盘绝对路径目录
        File dir = new File("src/main/resources/static/qualification");
        String realPath;
       try {
            realPath = dir.getCanonicalPath();
       }catch (Exception e){
           throw new InterrogationException("获取不到资格证存储位置",-1);
       }
        String absolutePath = realPath+ File.separator +baseName;
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdir();
        }
        try {
            qualification.transferTo(new File(absolutePath));
        }catch (Exception e){
            throw new InterrogationException(ResponseEnum.UPLOAD_ERROR);
        }
        HashMap<Object, Object> resultMap = new HashMap<>();

        resultMap.put("qualification",baseName);
        return ResponseResult.success().setMessage("上传成功").setData(resultMap);
    }

    @PostMapping("/deleteQualification/{qualification}")
    public ResponseResult deleteQualification(@PathVariable("qualification")
                                                          String qualification)  {
        try {
            File dir = new File("src/main/resources/static/qualification");
            String canonicalPath = dir.getCanonicalPath();
            Path path1 = Paths.get(canonicalPath + File.separator + qualification);
            Files.deleteIfExists(path1);
        } catch (IOException e) {
            throw new InterrogationException("删除失败或文件不存在",-1);
        }
        return ResponseResult.success();
    }

    @GetMapping("/getAuthenticateInfo/{userId}")
    public ResponseResult getInfo(@PathVariable("userId") Integer userId){
        DoctorInfoDTO authenticateInfo = authenticationInfoService.getAuthenticateInfo(userId);
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("authenticateInfo",authenticateInfo);
        return ResponseResult.success().setData(resultMap);
    }

    @PostMapping("/authenticate")
    public ResponseResult submitAuthenticationInfo(@RequestBody AuthenticationInfo authenticationInfo){
        log.info("认证信息{}",authenticationInfo);
        authenticationInfoService.submitAuthenticationInfo(authenticationInfo);
        return ResponseResult.success();
    }

    @GetMapping("/queryNumber/{doctorId}")
    public ResponseResult queryNumber(@PathVariable("userId") Integer doctorId){
        Long size = redisTemplate.opsForSet().size(doctorId+":queryUser");
        if (size > 5){
            throw new InterrogationException("医生问诊繁忙",-1);
        }
        return ResponseResult.success();
    }
    @GetMapping("/getDoctorName/{doctorId}")
    public ResponseResult getDoctorName(@PathVariable("doctorId") Integer doctorId){
        String doctorName = authenticationInfoService.getDoctorName(doctorId);
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("doctorName",doctorName);
        return ResponseResult.success().setData(resultMap);
    }
}
