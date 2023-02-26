package ru.javawebinar.topjava.repository.jpa;

import org.hibernate.Hibernate;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        if(meal.isNew()) {
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            if (em.find(Meal.class, meal.getId()).getUser().getId() == userId) {
                meal.setUser(user);
                return em.merge(meal);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> mealList = em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .getResultList();
        return DataAccessUtils.singleResult(mealList);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL_SORTED, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();

        /** через геттер у user, но работает медленнее **/
//        Hibernate.initialize(em.getReference(User.class, userId).getMealList());
//        List<Meal> mealList = em.getReference(User.class, userId).getMealList();
//        mealList.sort(Comparator.comparing(Meal::getDateTime).reversed());
//        return mealList;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_DATE_TIME_SORTED, Meal.class)
                .setParameter("start", startDateTime)
                .setParameter("end", endDateTime)
                .setParameter("user_id", userId)
                .getResultList();
    }
}