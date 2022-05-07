package com.team10.service;

import com.team10.framework.exception.MyException;
import org.springframework.stereotype.Service;

/**
 * @program: bankSys
 * @description:
 * @author: Mr. Su
 * @create: 2022-05-05 20:15
 **/
@Service
public class CheckParamService {
    //Check input amount is value number.
    public void checkAmount(String amount){
        //The pattern checks for amount with 1 dot: 3.56
        String regex1 = "(0|[1-9][0-9]*)\\.[0-9]{1,2}";
        //The pattern checks for amount with 0 dot: 3
        String regex2 = "(0|[1-9][0-9]*)";
        if (amount != null && (amount.matches(regex1) || amount.matches(regex2))) {
            checkAmount(Double.parseDouble(amount));
            return;
        }
        else{
            throw new MyException("invalid_input");
        }
    }
    //Check input amount is within the range.
    public void checkAmount(Double amount){
        if(amount >=0 && amount <=4294967295.99){
            return;
        }
        else {
            throw new MyException("invalid_input");
        }
    }
}
