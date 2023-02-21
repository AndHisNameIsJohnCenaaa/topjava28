package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_ID1, USER_ID);
        assertMatch(meal, userMeal1);
    }

    /** for user **/
    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND, USER_ID));
    }

    /** for user meal **/
    @Test
    public void delete() {
        service.delete(USER_MEAL_ID1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID1, USER_ID));
    }

    /** for user **/
    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND, USER_ID));
    }


    /** for user meal **/
    @Test
    public void getBetweenInclusive() {
        List<Meal> actual = service.getBetweenInclusive(
                LocalDate.of(2004, 10, 19),
                LocalDate.of(2004, 10, 19),
                USER_ID);
        assertMatch(actual, userMeal1, userMeal2, userMeal3);
    }

    /** for user meal **/
    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        assertMatch(actual, userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6);
    }

    @Test
    public void update() {
    }

    @Test
    public void create() {
    }

    @Test
    public void duplicateDateTimeCreate() {

    }
}