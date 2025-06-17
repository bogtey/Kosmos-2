package com.example.surveysystem.service;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.dto.SignupRequest;
import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Role;
import com.example.surveysystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
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
        man.setPseudonym(signupRequest.getPseudonym()); // Установка псевдонима
        man.setPassword(signupRequest.getSurname()); // Замените на закодированный пароль

        // Установка ролей
        Set<Role> roles = new HashSet<>();
        for (String roleName : signupRequest.getRoles()) {
            Role role = dataAccessLayer.getRoleByName(roleName); // Получаем роль из базы данных
            if (role != null) {
                roles.add(role);
            } else {
                // Обработка случая, когда роль не найдена
                // Например, можно создать новую роль или выбросить исключение
            }
        }
        man.setRoles(roles);

        return dataAccessLayer.newUserToDatabase(man);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Man user = dataAccessLayer.getUserFromDatabaseByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден с именем: " + username);
        }
        return UserDetailsImpl.build(user);
    }

    public Man loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        return dataAccessLayer.getUserFromDatabaseByUsername(username);
    }
}
