package com.ro.auth.service;

import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.oauth2.AuthProvider;
import com.ro.auth.oauth2.user.info.OAuth2UserInfo;
import com.ro.auth.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OAuth2Service {
    private final UserRepository userRepository;

    @Autowired
    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(OAuth2UserInfo oAuth2UserInfo, AuthProvider provider) {
        Optional<User> alreadyExistedUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (alreadyExistedUser.isPresent()) {
            throw new UserAlreadyExistException();
        }

        User user = new User();
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setName(oAuth2UserInfo.getName());
        user.setBonuses(0);
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
