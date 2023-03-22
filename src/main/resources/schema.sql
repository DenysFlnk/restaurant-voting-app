DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS meal;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255)         NOT NULL,
    email    VARCHAR(255)         NOT NULL,
    password VARCHAR(255)         NOT NULL,
    enabled  BOOLEAN DEFAULT TRUE NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER      NOT NULL,
    role    VARCHAR(255) NOT NULL,
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id     INTEGER PRIMARY KEY AUTO_INCREMENT,
    title  VARCHAR(255)         NOT NULL,
    active BOOLEAN DEFAULT TRUE NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_title_idx ON restaurant (title);

CREATE TABLE meal
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    meal_title VARCHAR(255) NOT NULL,
    price      INTEGER      NOT NULL
);
CREATE UNIQUE INDEX meal_unique_title_price_idx ON meal (meal_title, price);

CREATE TABLE vote
(
    user_id       INTEGER   NOT NULL,
    restaurant_id INTEGER   NOT NULL,
    date_time     TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE menu
(
    meal_id       INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    menu_date     DATE    NOT NULL,
    CONSTRAINT mealId_date_idx UNIQUE (meal_id, menu_date)
);
