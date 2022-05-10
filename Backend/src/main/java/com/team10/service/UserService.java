package com.team10.service;

import com.team10.entity.User;
import com.team10.framework.exception.MyException;
import com.team10.mapper.UserMapper;
import com.team10.utils.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CheckParamService checkParamService;

    @Value("${spring.datasource.username}")
    public String databaseUserName;
    @Value("${spring.datasource.password}")
    public String databasePassword;

    public User insecureLogin(String username, String password) {
        Connection connection = null;
        //Database password info
        String url = "jdbc:mysql://localhost:3306/bank?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        String name = databaseUserName; //"bankDev";
        String pwd = databasePassword; //"dev123";
        User user = null;

        try {
            //Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, name, pwd);
            /* START BAD CODE */
            if(innerValidUser(username, connection)){
                user = innerValidUser(username, password, connection);
                if(user == null){
                    throw new MyException("Login Failed - incorrect password");
                }
            }
            else{
                throw new MyException("Login Failed - unknown username");
            }
            /* END BAD CODE */
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }


    private boolean innerValidUser(String username, Connection connection) throws SQLException {
        User user = null;
        /* START BAD CODE */
        String sqlMyEvents = "select * from USER where username ='" + username
                 + "' limit 1; ";
        System.out.println(sqlMyEvents);
        Statement sqlStatement = null;
        sqlStatement = connection.createStatement();
        ResultSet rs = sqlStatement.executeQuery(sqlMyEvents);
        /* END BAD CODE */
        return rs.next();
    }



    private User innerValidUser(String username, String password, Connection connection) throws SQLException {
        User user = null;
        /* START BAD CODE */
        String sqlMyEvents = "select * from USER where username ='" + username
                + "' AND password = '" + password + "' limit 1; ";
        System.out.println(sqlMyEvents);
        Statement sqlStatement = null;
        sqlStatement = connection.createStatement();
        ResultSet rs = sqlStatement.executeQuery(sqlMyEvents);
        /* END BAD CODE */
        if(rs.next()){
            user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("username"));
            user.setBalance(rs.getDouble("balance"));
        }
        return user;
    }


    //Secure Login by using Mybatis that uses PreparedStatement
    public User login(String username, String password){
        return userMapper.login(Maps.build().put("username",username).put("password",password).getMap());
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
