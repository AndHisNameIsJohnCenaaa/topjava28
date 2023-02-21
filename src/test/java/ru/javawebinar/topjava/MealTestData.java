package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID1 = START_SEQ + 3;
    public static final int USER_MEAL_ID2 = START_SEQ + 4;
    public static final int USER_MEAL_ID3 = START_SEQ + 5;
    public static final int USER_MEAL_ID4 = START_SEQ + 6;
    public static final int USER_MEAL_ID5 = START_SEQ + 7;
    public static final int USER_MEAL_ID6 = START_SEQ + 8;
    public static final int ADMIN_MEAL_ID1 = START_SEQ + 9;
    public static final int ADMIN_MEAL_ID2 = START_SEQ + 10;
    public static final int ADMIN_MEAL_ID3 = START_SEQ + 11;
    public static final int GUEST_MEAL_ID1 = START_SEQ + 12;
    public static final int GUEST_MEAL_ID2 = START_SEQ + 13;
    public static final int GUEST_MEAL_ID3 = START_SEQ + 14;
    public static final int MEAL_NOT_FOUND = 100;

    public static final Meal userMeal1 = new Meal(USER_MEAL_ID1,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "завтрак", 1000);
    public static final Meal userMeal2 = new Meal(USER_MEAL_ID2,
            LocalDateTime.of(2004, 10, 19, 14, 13, 10), "обед", 500);
    public static final Meal userMeal3 = new Meal(USER_MEAL_ID3,
            LocalDateTime.of(2004, 10, 19, 19, 34, 1), "ужин", 499);
    public static final Meal userMeal4 = new Meal(USER_MEAL_ID4,
            LocalDateTime.of(2004, 10, 19, 9, 23, 54), "завтрак", 1000);
    public static final Meal userMeal5 = new Meal(USER_MEAL_ID5,
            LocalDateTime.of(2004, 10, 19, 13, 23, 54), "обед", 501);
    public static final Meal userMeal6 = new Meal(USER_MEAL_ID6,
            LocalDateTime.of(2004, 10, 19, 20, 23, 54), "ужин", 600);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_ID1,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "завтрак", 1000);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_ID2,
            LocalDateTime.of(2004, 10, 19, 14, 13, 10), "обед", 500);
    public static final Meal adminMeal3 = new Meal(ADMIN_MEAL_ID3,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "ужин", 500);
    public static final Meal guestMeal1 = new Meal(GUEST_MEAL_ID1,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "завтрак", 1000);
    public static final Meal guestMeal2 = new Meal(GUEST_MEAL_ID2,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "обед", 500);
    public static final Meal guestMeal3 = new Meal(GUEST_MEAL_ID3,
            LocalDateTime.of(2004, 10, 19, 10, 23, 54), "ужин", 501);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2010, 10, 19, 10, 23, 54), "new meal", 500);
    }

    /** for MEAL_ID1 **/
    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2010, 10, 21, 14, 50, 30));
        updated.setDescription("updated description");
        updated.setCalories(699);
        return updated;
    }

    /** for MEAL_ID1 **/
    public static Meal getDateTimeDuplicated() {
        return new Meal(LocalDateTime.of(2004, 10, 19, 10, 23, 54), "завтрак", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
