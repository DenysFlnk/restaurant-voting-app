# Restaurant voting App
> Voting App for choose restaurant where to lunch. Created with Spring Boot 3, Spring Data JPA, H2 DB, Spring Security, Open API 3. 

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Contact](#contact)

## General Information
Description taken from test task and used for app creation: 

>Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.
>
>The task is:
>
>Build a voting system for deciding where to have lunch.
>
>* 2 types of users: admin and regular users
>* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
>* Menu changes each day (admins do the updates)
>* Users can vote for a restaurant they want to have lunch at today
>* Only one vote counted per user
>* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
>
>Each restaurant provides a new menu each day.

## Technologies Used
- Java 17
- Spring Boot 3.0.6
- Spring Security
- Spring Data JPA
- H2 DB
- Caffeine cache
- Logback logging
- Testing:
  - JUnit 5
  - Mockito
  - Spring Security Test 6.1.0
- Open API 3 (Swagger)


## Features
App features:
- Basic Authentication
- Data binding, validation
- Global exception handling both app and security
- Response caching
- Logging
- Tests for app logic and security
- Documentation


User features:
- List all restaurants and vote for any if it's in time gate
- Browse vote results
- List all user's votes and modify them if it's in time gate

Admin features:
- Includes all user features
- Manage user accounts
- List all restaurants and create/modify them
- List all meals belong to restaurant and create/modify them

## Setup
Fork this repo to your github and clone to your PC. Run `RestaurantVotingApplication.class` in your IDE. 


## Usage
You can use and test every app feature with Swagger UI by clicking [link](http://localhost:8080/restaurant-voting/swagger-ui.html).

Log in credentials:
>username: user1@gmail.com,  password: 12345
> 
>username: user2@gmail.com,  password: abcd
> 
>username: admin1@gmail.com,  password: gqfqf123

## Contact
Created by Denys Filonenko:
- [GitHub](https://github.com/DenysFlnk)
- [Email](mailto:filonenko.denys94@gmail.com)
