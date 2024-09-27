package com.example.kanbanbackend.Auth;

import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.Exception.ErrorResponse;
import com.example.kanbanbackend.Exception.ErrorValidationResponse;
import com.example.kanbanbackend.Exception.ForBiddenException;
import com.example.kanbanbackend.Service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.SignatureException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;


        if(request.getRequestURI().equals("/login") || request.getRequestURI().equals("/token")) { //handle ให้ login โดยไม่มี token ได้
            chain.doFilter(request, response);
            return;
        }



        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (io.jsonwebtoken.SignatureException e) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid JWT signature", request);
                    return;
                } catch (ExpiredJwtException e) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT token is expired", request);
                    return;
                } catch (MalformedJwtException e) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Malformed JWT token", request);
                    return;
                } catch (UnsupportedJwtException e) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unsupported JWT token", request);
                    return;
                } catch (IllegalArgumentException e) {
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT token is missing or invalid", request);
                    return;
                }
                if(request.getMethod().equals("POST") && request.getRequestURI().equals("/v3/boards")) {
                    chain.doFilter(request, response);
                    return;
                }

                String boardId = request.getRequestURI().split("/").length >= 3 ? request.getRequestURI().split("/")[3] : request.getRequestURI().split("/")[2];
                Claims claims = jwtTokenUtil.decodedToken(request);

                if(permission.getRoleOfBoard(boardId,claims.get("oid").toString())){ //handle ให้ role owner เข้าใช้งานได้เลย
                    chain.doFilter(request, response);
                    return;
                }

                if(request.getRequestURI().contains("/v3/boards/user")) { //handle getBoardLiast ของ user เพื่อไม่ไปทับลายกับ get method อื่นๆด้านล่าง
                    chain.doFilter(request, response);
                    return;
                }

                if(request.getMethod().equals("GET") && visibilityConfig.visibilityType(boardId)){ //handle กรณีไม่ login เข้ามาและดู board public ได้เลย
                    chain.doFilter(request, response);
                    return;
                }
                else {
                    sendErrorResponse(response, HttpStatus.FORBIDDEN,"You do not have permission to access this resource", request);
                    return;
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT Token does not begin with Bearer String");
            }
        }else if(requestTokenHeader == null) {
            String boardId = request.getRequestURI().split("/")[3];
            if(request.getMethod().equals("GET") && visibilityConfig.visibilityType(boardId)){
                chain.doFilter(request, response);
                return;
            }else {
                sendErrorResponse(response, HttpStatus.FORBIDDEN,"You do not have permission to access this resource", request);
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message, HttpServletRequest request) throws IOException {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        ErrorValidationResponse errorValidationResponse = new ErrorValidationResponse(status.value(), message, null);
        ErrorResponse errorResponse = new ErrorResponse(timeStamp,status.value(),status.getReasonPhrase(),message,request.getRequestURI());
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
