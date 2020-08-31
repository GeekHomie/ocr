package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.service.IAuthService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ethan
 * @description 用户登陆模块
 * @date 8/25 10:41
 */
@Api("授权模块")
@RestController
public class AuthController {

    @Autowired
    IAuthService authService;

    @ApiOperation("用户账号密码登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) throws JSONException {
        boolean checked = authService.login(username, password);
        return booleanLoginMessage(checked);

    }

    private String booleanLoginMessage(boolean checked) throws JSONException {
        JSONObject json = new JSONObject();
        if (checked){
            json.put("ret", "ok");
            json.put("msg", "login success");
            return json.toString();
        }
        json.put("ret", "err");
        json.put("msg", "login fail");
        return json.toString();
    }
}
