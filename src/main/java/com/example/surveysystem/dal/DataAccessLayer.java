package com.example.surveysystem.dal;

import com.example.surveysystem.models.Man;
import com.example.surveysystem.models.Role;
import com.example.surveysystem.models.Survey;
import com.example.surveysystem.repositories.ManRepository;
import com.example.surveysystem.repositories.RoleRepository;
import com.example.surveysystem.repositories.SurveyRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class DataAccessLayer {
    private final SessionFactory sessionFactory;
    private final RoleRepository roleRepository;
    private final ManRepository manRepository; // Добавляем manRepository
    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory, RoleRepository roleRepository, ManRepository manRepository) {
        this.sessionFactory = sessionFactory;
        this.roleRepository = roleRepository;
        this.manRepository = manRepository; // Инициализация manRepository
    }

    // Метод для создания опроса
    public void createSurvey(Survey newSurvey) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(newSurvey);
            session.getTransaction().commit();
        } catch (Exception e) {
            // Обработка исключений
            e.printStackTrace();
        }
    }
    @Autowired
    private SurveyRepository surveyRepository; // Внедряем SurveyRepository
    public boolean surveyExists(long id) {
        return surveyRepository.existsById(id); // Используем экземпляр surveyRepository
    }


    // Метод для удаления опроса
    public void deleteSurvey(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Survey survey = session.get(Survey.class, id);
            if (survey != null) {
                session.remove(survey);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для обновления опроса
    public void updateSurvey(Long id, Survey newSurvey) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Survey survey = session.get(Survey.class, id);
            if (survey != null) {
                survey.setQuestion(newSurvey.getQuestion());
                survey.setTitle(newSurvey.getTitle());
                survey.setNote(newSurvey.getNote());
                session.update(survey);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для получения опроса
    public Survey getSurvey(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Survey survey = session.get(Survey.class, id);
            session.getTransaction().commit();
            return survey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для получения всех опросов
    public List<Survey> getSurveys() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Survey> query = builder.createQuery(Survey.class);
            Root<Survey> root = query.from(Survey.class);
            query.select(root);
            List<Survey> resultList = session.createQuery(query).getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для создания пользователя
    public void createMan(Man newMan) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(newMan);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления пользователя
    public void deleteMan(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Man man = session.get(Man.class, id);
            if (man != null) {
                session.remove(man);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для обновления пользователя
    public void updateMan(Long id, Man newMan) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Man man = session.get(Man.class, id);
            if (man != null) {
                man.setName(newMan.getName());
                man.setSurname(newMan.getSurname());
                man.setAge(newMan.getAge());
                man.setPseudonym(newMan.getPseudonym());
                session.update(man);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для получения пользователя
    public Man getMan(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Man man = session.get(Man.class, id);
            session.getTransaction().commit();
            return man;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для получения всех пользователей
    public List<Man> getMans() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Man> query = builder.createQuery(Man.class);
            Root<Man> root = query.from(Man.class);
            query.select(root);
            List<Man> resultList = session.createQuery(query).getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для добавления нового пользователя с ролью USER
    public String newUserToDatabase(Man man) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String name = man.getName();

            // Проверяем, существует ли пользователь с таким именем
            Query<Man> query = session.createQuery("FROM Man where name = :name", Man.class);
            query.setParameter("name", name);
            Man userFrom = query.uniqueResult();

            if (userFrom != null) {
                return "Выберите другое имя";
            }

            // Получаем роль USER
            Role userRole = findRoleByName("ROLE_USER"); // Используем метод findRoleByName
            if (userRole != null) {
                man.getRoles().add(userRole); // Назначаем роль пользователю
            }

            // Сохраняем пользователя в базе данных
            session.persist(man);
            session.getTransaction().commit();
            return "Пользователь успешно создан";
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при создании пользователя";
        }
    }
    public Role getRoleByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Role> query = session.createQuery("FROM Role WHERE name = :name", Role.class);
            query.setParameter("name", roleName);
            Role role = query.uniqueResult();
            session.getTransaction().commit();
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Man> getAllMans() {
        return manRepository.findAll(); // Предполагается, что у вас есть manRepository для работы с сущностью Man
    }

    // Метод для получения пользователя по имени
    public Man getUserFromDatabaseByUsername(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Man> query = session.createQuery("FROM Man where name = :name", Man.class);
            query.setParameter("name", name);
            Man userFrom = query.uniqueResult();
            return userFrom;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для получения опросов по ID пользователя
    public List<Survey> getSurveysByManId(Long manId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.createQuery("SELECT b FROM Survey b WHERE b.man.id = :manId", Survey.class)
                    .setParameter("manId", manId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для проверки существования псевдонима
    public boolean checkPseudonymExists(String pseudonym) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(m) FROM Man m WHERE m.pseudonym = :pseudonym", Long.class);
            query.setParameter("pseudonym", pseudonym);
            Long count = query.uniqueResult();
            session.getTransaction().commit();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для поиска роли по имени
    public Role findRoleByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Role> query = session.createQuery("FROM Role WHERE name = :name", Role.class);
            query.setParameter("name", roleName);
            Role role = query.uniqueResult();
            session.getTransaction().commit();
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
