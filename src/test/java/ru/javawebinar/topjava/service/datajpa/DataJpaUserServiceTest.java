package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;
@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = {Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMealList() {
        User expected = user;
        expected.setMealList(List.of(meal1, meal2, meal3, meal4, meal5, meal6, meal7));
        expected.getMealList().forEach(meal -> meal.setUser(user));

        User actual = service.getWithMealList(USER_ID);
        USER_MATCHER.assertMatch(actual, expected);
        MEAL_MATCHER.assertMatch(actual.getMealList(), expected.getMealList());
    }
}
