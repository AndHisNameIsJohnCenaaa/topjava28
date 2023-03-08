package ru.javawebinar.topjava.service.datajpa;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = {Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal expected = MealTestData.meal1;
        expected.setUser(UserTestData.user);
        Meal actual =  service.getWithUser(MEAL1_ID, USER_ID);
        MEAL_MATCHER.assertMatch(actual, expected);
        USER_MATCHER.assertMatch((User) Hibernate.unproxy(actual.getUser()), expected.getUser());
    }
}
