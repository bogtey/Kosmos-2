package com.example.surveysystem.dal;
import com.example.surveysystem.models.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Getter
public class DataAccessLayer {
    private final SessionFactory sessionFactory;

    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Session session = null;
    public void createSurvey(Survey newSurvey) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newSurvey);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteSurvey(Long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        Survey survey = session.get(Survey.class, id);
        session.remove(survey);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updateSurvey(Long id, Survey newSurvey){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Survey survey = session.get(Survey.class, id);
        survey.setQuestion(newSurvey.getQuestion());
        survey.setTitle(newSurvey.getTitle());
        session.merge(survey);
        session.getTransaction().commit();
    }
    public Survey getSurvey(Long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        Survey survey = session.get(Survey.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return survey;
    }
    public List<Survey> getSurveys(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Survey> query = builder.createQuery(Survey.class);
        Root<Survey> root = query.from(Survey.class);
        query.select(root);
        List<Survey> resultList = session.createQuery(query).getResultList();
        return resultList;
    }




    public void createMan(Man newMan) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newMan);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void deleteMan(Long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        Man man = session.get(Man.class, id);
        session.remove(man);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    public void updateMan(Long id, Man newMan){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Man man = session.get(Man.class, id);
        man.setName(newMan.getName());
        man.setSurname(newMan.getSurname().toString());
        man.setAge(newMan.getAge());
        session.merge(man);
        session.getTransaction().commit();
    }
    public Man getMan(Long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        Man man = session.get(Man.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return man;
    }
    public List<Man> getMans(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Man> query = builder.createQuery(Man.class);
        Root<Man> root = query.from(Man.class);
        query.select(root);
        List<Man> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    public String newUserToDatabase(Man man) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String name = man.getName();

        Query query = session
                .createQuery("FROM Man where name = :name")
                .setParameter("name", name);
        Man userFrom = (Man) query.uniqueResult();

        if (userFrom != null) {
            return "Выберите другое имя";
        }
        session.persist(man);
        session.getTransaction().commit();
        session.close();
        return "Pabeda)";
    }

    public Man getUserFromDatabaseByUsername(String name) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session
                .createQuery("FROM Man where name = :name")
                .setParameter("name", name);
        Man userFrom = (Man) query.uniqueResult();
        if (userFrom == null) {
            return null;
        }
        return userFrom;
    }

    public List<Survey> getSurveysByManId(Long manId) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.createQuery("SELECT b FROM Survey b WHERE b.man.id = :manId", Survey.class)
                .setParameter("manId", manId)
                .getResultList();
    }
}
