package com.team10.service;

import com.team10.framework.exception.MyException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

/**
 * @program: BankSystem
 * @description: Check IP
 * @author: Mr. Su
 * @create: 2022-05-09 21:40
 **/
@Service
public class IpService {

    final static HashSet<String> whiteIps = new HashSet<>();

    static {
        whiteIps.add("127.0.0.1");
        whiteIps.add("0:0:0:0:0:0:0:1");
        whiteIps.add("localhost");
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // Take the IP configured on the local machine according to the NIC
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }

            // For the case of multiple proxies, the first IP is the real IP of the client, and multiple IPs are split by ','
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    public static void checkOriginIP(HttpServletRequest request){
        String ip = getIpAddr(request);
        System.out.println("ip: " + ip);
        if(!whiteIps.contains(ip)){
            throw new MyException("Illegal Origin");
        }
    }
}
