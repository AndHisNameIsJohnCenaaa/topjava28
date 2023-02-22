DELETE FROM user_role;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(user_id, date_time, description, calories)
VALUES (100000, '2004-10-19 10:23:54', 'завтрак', 1000),
       (100000, '2004-10-19 14:13:10', 'обед', 500),
       (100000, '2004-10-19 19:34:01', 'ужин', 499),
       (100000, '2004-10-20 9:23:54', 'завтрак', 1000),
       (100000, '2004-10-20 13:23:54', 'обед', 501),
       (100000, '2004-10-20 20:23:54', 'ужин', 600),
       (100001, '2004-10-19 10:23:54', 'завтрак', 1000),
       (100001, '2004-10-19 14:13:10', 'обед', 500),
       (100001, '2004-10-19 19:34:01', 'ужин', 500),
       (100002, '2004-10-19 10:23:54', 'завтрак', 1000),
       (100002, '2004-10-19 14:13:10', 'обед', 500),
       (100002, '2004-10-19 19:34:01', 'ужин', 501);