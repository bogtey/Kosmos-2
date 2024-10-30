package com.example.surveysystem.service;


import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.dto.SignupRequest;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {
    private final DataAccessLayer dataAccessLayer;
    @Autowired
    public UserDetailsServiceImpl(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }
    public String newUser (SignupRequest signupRequest) {
        Man man = new Man();
        man.setName(signupRequest.getName());
        man.setSurname(signupRequest.getSurname());
        man.setPassword(signupRequest.getSurname()); // Здесь, вероятно, нужно использовать закодированный пароль
        man.setPseudonym(signupRequest.getPseudonym()); // Установка псевдонима
        man.setRoles(signupRequest.getRoles());
        return dataAccessLayer.newUserToDatabase(man);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Man user = dataAccessLayer.getUserFromDatabaseByUsername(username);
        if (user == null) return null;
        return UserDetailsImpl.build(user);
    }
    public Man loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        return dataAccessLayer.getUserFromDatabaseByUsername(username);
    }
}
