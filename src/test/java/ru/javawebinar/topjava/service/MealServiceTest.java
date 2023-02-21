package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.DataFormatException;

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

    /** for user meal **/
    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> service.create(getDateTimeDuplicated(), USER_ID));
    }
}