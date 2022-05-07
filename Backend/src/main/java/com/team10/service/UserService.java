package com.team10.service;

import com.team10.entity.User;
import com.team10.framework.exception.MyException;
import com.team10.mapper.UserMapper;
import com.team10.utils.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CheckParamService checkParamService;

    public User login(String username, String password){
        return userMapper.detail(Maps.build().put("username",username).put("password",password).getMap());
    }
    public int create(User user){
        return userMapper.create(user);
    }
    public Double query(Integer id){
        return userMapper.query(id);
    }

    /*
    * @id user id
    * @value deposit money
    * */
    public Double deposit(Integer id, String amountStr){
        checkParamService.checkAmount(amountStr);
        Double balance = query(id);
        balance = balance + Double.parseDouble(amountStr);
        updateBalance(id, balance);
        return query(id);
    }

    public Double withdrawal(Integer id, String amountStr){
        checkParamService.checkAmount(amountStr);
        Double balance = query(id);
        Double amount = Double.parseDouble(amountStr);
        if (balance-amount<0){
            throw new MyException("You don't have sufficient balance");
        }
        balance = balance - Double.parseDouble(amountStr);
        updateBalance(id, balance);
        System.out.println();
        return query(id);
    }

    public void updateBalance(Integer id, Double balance){
        userMapper.updateBalance(id, balance);
        return;
    }
}
