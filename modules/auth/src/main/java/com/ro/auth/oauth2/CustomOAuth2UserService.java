package com.ro.auth.oauth2;

import com.ro.auth.data.model.AuthProvider;
import com.ro.auth.data.model.User;
import com.ro.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.ro.auth.oauth2.user.info.OAuth2UserInfo;
import com.ro.auth.oauth2.user.info.OAuth2UserInfoFactory;
import com.ro.auth.oauth2.user.loaders.VkUserInfoLoader;
import com.ro.auth.data.repository.UserRepository;
import com.ro.auth.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final OAuth2Service oAuth2Service;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, OAuth2Service oAuth2Service) {
        this.userRepository = userRepository;
        this.oAuth2Service = oAuth2Service;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User;
        if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase(AuthProvider.VK)) {
            oAuth2User = new VkUserInfoLoader().loadUser(oAuth2UserRequest);
        } else {
            oAuth2User = super.loadUser(oAuth2UserRequest);
        }

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.getClientRegistration().getRegistrationId(),
            oAuth2User.getAttributes());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        String oAuth2Provider = oAuth2UserRequest.getClientRegistration()
                .getRegistrationId().toUpperCase();

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if(!user.getAuthProvider().getName().equals(oAuth2Provider)) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        oAuth2Provider + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
            return oAuth2Service.updateUser(user, oAuth2UserInfo);
        } else {
            return oAuth2Service.registerUser(oAuth2UserInfo, oAuth2Provider);
        }
    }
}
