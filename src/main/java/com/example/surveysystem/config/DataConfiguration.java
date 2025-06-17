package com.example.surveysystem.config;

import com.example.surveysystem.dal.DataAccessLayer;
import com.example.surveysystem.models.Role;
import com.example.surveysystem.repositories.RoleRepository;
import com.example.surveysystem.repositories.ManRepository; // Импортируйте ManRepository
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfiguration {
    private final SessionFactory sessionFactory;
    private final RoleRepository roleRepository;
    private final ManRepository manRepository; // Добавьте это поле

    @Autowired
    public DataConfiguration(EntityManager entityManager, RoleRepository roleRepository, ManRepository manRepository) {
        this.roleRepository = roleRepository;
        this.manRepository = manRepository; // Инициализация manRepository
        Session session = entityManager.unwrap(Session.class);
        this.sessionFactory = session.getSessionFactory();
    }

    @Bean
    public DataAccessLayer dataAccessLayer() {
        return new DataAccessLayer(sessionFactory, roleRepository, manRepository); // Передаем все три параметра
    }

    @PostConstruct
    public void initRoles() {
        // Инициализация ролей
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
    }
}
