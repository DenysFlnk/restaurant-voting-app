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

INSERT INTO RESTAURANT (TITLE, ACTIVE)
VALUES ('Bistro', true),
       ('Cafeteria', true),
       ('Restaurant', true),
       ('Inactive restaurant', false);

INSERT INTO MEAL (RESTAURANT_ID, MEAL_TITLE, PRICE)
VALUES (1, 'Tea', 2),
       (2, 'Coffee', 3),
       (3, 'Vine', 5),
       (1, 'Salad', 5),
       (2, 'Croissant', 4),
       (3, 'Steak', 8);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, DATE_TIME)
VALUES (1, 2, TIMESTAMPADD(MINUTE , 570, CURRENT_DATE)),
       (2, 2, TIMESTAMPADD(MINUTE , 540, CURRENT_DATE)),
       (3, 1, TIMESTAMPADD(MINUTE , 654, CURRENT_DATE));


