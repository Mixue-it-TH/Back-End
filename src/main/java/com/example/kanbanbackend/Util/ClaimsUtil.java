package com.example.kanbanbackend.Util;

import com.example.kanbanbackend.Auth.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimsUtil {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public  Claims getClaims(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7).trim();
        return  jwtTokenUtil.getAllClaimsFromToken(token);
    }
}
