package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final ResultSetExtractor<List<User>> resultSetExtractor = new ResultSetExtractor<List<User>>() {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                User user = userMap.get(id);
                if (user == null) {
                    user = new User();
                    user.setId(id);
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setRegistered(rs.getDate("registered"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    user.setRoles(new HashSet<>());
                    String role = rs.getString("role");
                    if (role != null) {
                        user.getRoles().add(Role.valueOf(role));
                    }
                    userMap.putIfAbsent(id, user);
                } else {
                    String role = rs.getString("role");
                    if (role != null) {
                        user.getRoles().add(Role.valueOf(role));
                    }
                }
            }
            return new ArrayList<>(userMap.values());
        }
    };

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("INSERT INTO user_role (role, user_id) VALUES(?, ?)",
                    user.getRoles(),
                    user.getRoles().size(),
                    (ps, role) -> {
                        ps.setString(1, role.toString());
                        ps.setInt(2, user.getId());
                    });
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0 &
                jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId())== 0 &
                Arrays.stream(jdbcTemplate.batchUpdate("INSERT INTO user_role (role, user_id) VALUES(?, ?) ON CONFLICT DO NOTHING ",
                        user.getRoles(),
                        user.getRoles().size(),
                        (ps, role) -> {
                            ps.setString(1, role.toString());
                            ps.setInt(2, user.getId());

                        })).noneMatch(row -> row.length > 0)) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT OUTER JOIN user_role ur on u.id = ur.user_id WHERE id=?", resultSetExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_role ur on users.id = ur.user_id WHERE email=?", resultSetExtractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT OUTER JOIN user_role ur " +
                "on u.id = ur.user_id ORDER BY name, email", resultSetExtractor);
    }
}
