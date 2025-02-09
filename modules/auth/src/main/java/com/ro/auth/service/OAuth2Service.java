package com.ro.auth.service;

import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.data.model.AuthProvider;
import com.ro.auth.data.model.User;
import com.ro.auth.oauth2.exception.OAuth2ProviderNotExistException;
import com.ro.auth.oauth2.user.info.OAuth2UserInfo;
import com.ro.auth.data.repository.AuthProviderRepository;
import com.ro.auth.data.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OAuth2Service {
    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    @Autowired
    public OAuth2Service(UserRepository userRepository,
                         AuthProviderRepository authProviderRepository) {
        this.userRepository = userRepository;
        this.authProviderRepository = authProviderRepository;
    }

    @Transactional
    public User registerUser(OAuth2UserInfo oAuth2UserInfo, String authProvider) {
        Optional<User> alreadyExistedUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (alreadyExistedUser.isPresent()) {
            throw new UserAlreadyExistException();
        }

        AuthProvider provider = authProviderRepository.findByName(authProvider)
            .orElseThrow(() -> new OAuth2ProviderNotExistException(authProvider));

        User user = new User();
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setName(oAuth2UserInfo.getName());
        user.setAuthProvider(provider);
        user.setIsCredentialsExpired(true);

        User savedUser = userRepository.save(user);
        Hibernate.initialize(savedUser.getAuthorities());

        return savedUser;
    }

    @Transactional
    public User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());

        User savedUser = userRepository.save(user);
        Hibernate.initialize(savedUser.getAuthorities());

        return savedUser;
    }
}
