package com.example.kanbanbackend.Auth;

import com.example.kanbanbackend.Entitites.Primary.User;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println(authentication);
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> oauth2UserAttributes = oauth2User.getAttributes();

        User user = new User(
                oauth2UserAttributes.get("oid").toString(),
                (String) oauth2UserAttributes.get("name"),
                (String) oauth2UserAttributes.get("email")
        );

        User primaryUser = primaryUserRepository.findUsersByOid(user.getOid());
        if (primaryUser == null) {
            primaryUser = new User(user.getOid(), user.getUserName(), user.getEmail());
            primaryUserRepository.save(primaryUser);
        }

        String redirectAfterLogin = (String) request.getSession().getAttribute("redirect");
        System.out.println(redirectAfterLogin);

        response.sendRedirect(redirectAfterLogin);
    }





}
