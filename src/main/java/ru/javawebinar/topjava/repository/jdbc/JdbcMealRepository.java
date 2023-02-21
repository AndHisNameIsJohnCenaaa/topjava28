package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;



    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            MapSqlParameterSource map = new MapSqlParameterSource()
                    .addValue("user_id", userId)
                    .addValue("id", meal.getId())
                    .addValue("date_time", meal.getDateTime())
                    .addValue("description", meal.getDescription())
                    .addValue("calories", meal.getCalories());
            Number newKey = insert.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (jdbcTemplate.update("update meals set date_time = ?, description = ?, calories = ? where user_id = ? and id = ?",
                meal.getDateTime(), meal.getDescription(), meal.getCalories(), userId, meal.getId()) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE user_id = ? and id = ?", userId, id) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("select * from meals where user_id = ? and id = ?", ROW_MAPPER, userId, id);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("select * from meals where user_id = ? order by date_time", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("select * from meals where user_id = ? and date_time >= ? and date_time <= ?",
                ROW_MAPPER, userId, startDateTime, endDateTime);
    }
}
