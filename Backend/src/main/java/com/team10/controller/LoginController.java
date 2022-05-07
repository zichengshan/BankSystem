package com.team10.controller;


import com.team10.entity.User;
import com.team10.framework.jwt.JWTUtil;
import com.team10.service.UserService;
import com.team10.utils.Maps;
import com.team10.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        User user=userService.login(username,password);
        if(user!=null){
            String token=JWTUtil.sign(user);
            return Result.ok(Maps.build().put("token",token).put("user",user).getMap());
        }else{
            return Result.fail("Username or password is wrong");
        }
    }
}
