package com.team10.mapper;

import com.team10.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserMapper {
    public User detail(Map<String,Object> paramMap);
    public int create(User user);
    public Double query(int id);
    public int updateBalance(int id, Double balance);
}
