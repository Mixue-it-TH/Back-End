package com.example.kanbanbackend.Auth;

import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.Exception.ErrorResponse;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        if (request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }


        if (request.getRequestURI().equals("/login")) { //handle ให้ login โดยไม่มี token ได้
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

                String boardId = request.getRequestURI().split("/").length >= 4 ? request.getRequestURI().split("/")[3] : null;
                if (boardId != null) {

                    Claims claims = jwtTokenUtil.decodedToken(request);
                    if (permission.getPermissionOfBoard(boardId, claims.get("oid").toString(),request.getMethod(),request.getRequestURI())){
                        chain.doFilter(request, response);
                        return;
                    }

                    //handle getBoardLiast ของ user เพื่อไม่ไปทับลายกับ get method อื่นๆด้านล่าง
                    if (request.getRequestURI().contains("/v3/boards/user")) {
                        chain.doFilter(request, response);
                        return;
                    }

                    // HANDLE เขียนเพิ่มให้หน่อย ถ้าหาก method เป็น GET และเป็น path ของ /boards/xxxxx/collabs ให้ผ่านไปเลย

                    // handel  /boards/xxxxx/collabs
                    if(permission.getNewPermissionCollab(boardId, claims.get("oid").toString(),request.getMethod(),request.getRequestURI())){
                        chain.doFilter(request, response);
                        return;
                    }

                    // handle Invitations
                    if (request.getRequestURI().contains("invitations")) {
                        try {
                            if (permission.getPermissionOfInvitation(boardId, claims.get("oid").toString(), request.getMethod(), request.getRequestURI())) {
                                chain.doFilter(request, response);
                                return;
                            } else {
                                sendErrorResponse(response, HttpStatus.FORBIDDEN, "You do not have permission to access this invitation", request);
                                return;
                            }
                        } catch (ItemNotFoundException e) {
                            sendErrorResponse(response, HttpStatus.NOT_FOUND, e.getMessage(), request);
                            return;
                        }
                    }

                    try {
                        if (visibilityConfig.visibilityType(boardId) && request.getMethod().equals("GET")) {
                                chain.doFilter(request, response);
                                return;
                        }
                        else {
                            sendErrorResponse(response, HttpStatus.FORBIDDEN, "You do not have permission to access this resource", request);
                            return;
                        }
                    } catch (ItemNotFoundException e) {
                        // ส่ง 404 เมื่อ boardId ไม่เจอ
                        sendErrorResponse(response, HttpStatus.NOT_FOUND, e.getMessage(), request);
                        return;
                    }


                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JWT Token does not begin with Bearer String");
            }
        } else if (requestTokenHeader == null) {
            String boardId = request.getRequestURI().split("/")[3];

            try {
                //handle กรณีไม่ login เข้ามาและดู board public ได้เลย
                if (request.getMethod().equals("GET") && visibilityConfig.visibilityType(boardId)) {
                    chain.doFilter(request, response);
                    return;
                } else if(request.getRequestURI().endsWith("collabs") && request.getMethod().equals("GET")){
                    chain.doFilter(request, response);
                    return;
                }

                else if(!request.getMethod().equals("GET")){
                    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "You have to login to do this service", request);
                    return;
                }
                else {
                    sendErrorResponse(response, HttpStatus.FORBIDDEN, "You do not have permission to access this resource", request);
                    return;
                }
            } catch (ItemNotFoundException e) {
                // ส่ง 404 เมื่อ boardId ไม่เจอ
                sendErrorResponse(response, HttpStatus.NOT_FOUND, e.getMessage(), request);
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
        ErrorResponse errorResponse = new ErrorResponse(timeStamp, status.value(), status.getReasonPhrase(), message, request.getRequestURI());
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
