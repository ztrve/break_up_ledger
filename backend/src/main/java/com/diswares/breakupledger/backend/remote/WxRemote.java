package com.diswares.breakupledger.backend.remote;

import com.diswares.breakupledger.backend.vo.wx.WxJsCode2SessionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信开放接口
 *
 * @author: z_true
 * @date: 2022/7/26 14:58
 * @version: 1.0.0
 */
@FeignClient(name = "baidu",url = "https://api.weixin.qq.com/sns")
public interface WxRemote {
    /**
     * 通过用户 code 换取 session
     * wx auth.code2Session
     *
     *
     * @example https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
     * @doc https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     * @param appid 小程序 appId
     * @param secret 小程序 appSecret
     * @param grantType 登录时获取的 code
     * @param jsCode 授权类型，此处只需填写 authorization_code
     * @return 用户信息
     */
    @GetMapping(value=  "/jscode2session")
    String jsCode2Session(@RequestParam("appid") String appid, @RequestParam("secret") String secret, @RequestParam("js_code") String jsCode, @RequestParam("grant_type") String grantType);
}
