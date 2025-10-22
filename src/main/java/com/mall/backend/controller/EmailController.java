package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.service.EmailService;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;


    /**
     * 发送邮件验证码
     * @param  email
     * @return
     */
    @PostMapping("/send")
    @SystemLog(module = "邮箱", operation = "发送邮件验证码")
    public Result sendEmailCode(@RequestParam("email") String email){
        emailService.sendVerificationCode( email);
        return Result.success("发送成功");
    }

    /**
     * 验证邮件验证码
     * @param  email
     * @param  code
     * @return
     */
    @PostMapping("/validate")
    public Result validateEmailCode(@RequestParam("email") String email, @RequestParam("code") String code){
        boolean validate = emailService.validate(email, code);
        if( validate){
            return Result.success("验证成功");
        }else {
            return Result.fail("验证失败");
        }
    }
}
