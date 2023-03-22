DELETE FROM MENU;
DELETE FROM VOTE;
DELETE FROM USER_ROLE;
DELETE FROM MEAL;
DELETE FROM RESTAURANT;
DELETE FROM USERS;

INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user1', 'user1@gmail.com', '12345'),
       ('user2', 'user2@gmail.com', 'abcd'),
       ('admin1', 'admin1@gmail.com', 'gqfqf123');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (3, 'ADMIN');

INSERT INTO RESTAURANT (TITLE)
VALUES ('Bistro'),
       ('Cafeteria'),
       ('Restaurant');

INSERT INTO MEAL (MEAL_TITLE, PRICE)
VALUES ('Tea', 2),
       ('Coffee', 3),
       ('Vine', 5),
       ('Salad', 5),
       ('Croissant', 4),
       ('Steak', 8);

INSERT INTO MENU (MEAL_ID, RESTAURANT_ID, MENU_DATE)
VALUES (1, 1, CURRENT_DATE),
       (2, 2, CURRENT_DATE),
       (3, 3, CURRENT_DATE),
       (4, 1, CURRENT_DATE),
       (5, 2, CURRENT_DATE),
       (6, 3, CURRENT_DATE);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, DATE_TIME)
VALUES (1, 2, CURRENT_TIMESTAMP()),
       (2, 2, CURRENT_TIMESTAMP()),
       (3, 1, CURRENT_TIMESTAMP());


