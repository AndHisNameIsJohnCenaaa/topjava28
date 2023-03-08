package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = {Profiles.HSQL_DB, Profiles.JDBC})
public class JdbcMealServiceTest extends AbstractMealServiceTest {
}
