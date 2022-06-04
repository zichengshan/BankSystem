package com.team10.controller;

import com.team10.entity.User;
import com.team10.framework.exception.MyException;
import com.team10.service.IpService;
import com.team10.service.UserService;
import com.team10.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
//    @Autowired
//    private ;

    @PostMapping("/create")
    public Result create(@RequestBody User user) {
        user.setBalance(0);
        userService.create(user);
        return Result.ok(user);
    }
    @PostMapping("/query")
    public Result query(@RequestBody Map<String,String> map) {
        Integer id = Integer.parseInt(map.get("id"));
        Double balance = userService.query(id);
        return Result.ok(balance);
    }

    @PostMapping("/deposit")
    public Result deposit(@RequestBody Map<String,String> map, HttpServletRequest request) {
        Integer id = Integer.parseInt(map.get("id"));
        checkUserPrivilege(request, id);
        Double balance = userService.deposit(id, map.get("amount"));
        return Result.ok(balance);
    }

    private void checkUserPrivilege(HttpServletRequest request, Integer id) {
        User user = (User) request.getAttribute("user");
        System.out.println("Authorization user is " + user.getId());
        System.out.println("Deposit action is operated in user: " + id);
//        IpService.checkOriginIP(request);
        //Start fix code
        //Check user has privilege to deposit id
        if(user.getId() != id){
            throw new MyException("Invalid Id");
        }
        //End Fix code
    }

    @PostMapping("/withdrawal")
    public Result withdrawal(@RequestBody Map<String,String> map, HttpServletRequest request) {
        Integer id = Integer.parseInt(map.get("id"));
        checkUserPrivilege(request, id);
        Double balance = userService.withdrawal(id, map.get("amount"));
        return Result.ok(balance);
    }



}
