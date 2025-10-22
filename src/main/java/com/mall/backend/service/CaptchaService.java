package com.mall.backend.service;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    //生成验证码
    public AbstractCaptcha generate(){
        //1.生成验证码
        AbstractCaptcha captcha =CaptchaUtil.createLineCaptcha(200, 100, 4, 20);
        //2.返回验证码
        return captcha;
    }
}
