# Filmorate

![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-green.svg?style=flat&logo=spring&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-grey.svg?style=flat&logo=h2&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-blue.svg?style=flat&logo=apache-maven&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit-5-blue.svg?style=flat&logo=junit5&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37.svg?style=flat&logo=postman&logoColor=white)

## Описание проекта

**Filmorate** — это бэкенд-сервис для платформы по управлению фильмами и пользовательскими оценками, разработанный на **Java** с использованием **Spring Boot**. Проект демонстрирует построение **RESTful API**, работу с реляционной базой данных и реализацию бизнес-логики, связанной с фильмами, пользователями и их взаимодействием.

![Пример работы Filmorate](https://code.s3.yandex.net/Java/Peresborka/10project/Comp-1-3.gif?etag=fdcd9ede1ee1bce2f751a297aa99c6a3)

Главная цель сервиса — помочь пользователям находить интересные фильмы, делиться мнениями с друзьями, ставить лайки и получать рекомендации на основе популярности фильмов.

## Ключевые технологии

*   **Java 21**
*   **Spring Boot 3.2.2**
*   **Spring JDBC** (для взаимодействия с базой данных)
*   **H2 Database** (встраиваемая файловая СУБД)
*   **Maven** (управление сборкой и зависимостями)
*   **Lombok** (сокращение шаблонного кода)
*   **Jakarta Validation** (валидация входящих данных)
*   **JUnit 5** (для юнит- и интеграционного тестирования)

## Архитектура и реализованные возможности

Проект построен на классической трехслойной архитектуре (**Controller -> Service -> DAO**):

*   **RESTful API:** Реализованы эндпоинты для всех основных сущностей: `users`, `films`, `genres`, `mpa`.
*   **Слой доступа к данным (DAO):** Весь код для работы с базой данных инкапсулирован в `...DbStorage` классах, которые используют `JdbcTemplate` для выполнения SQL-запросов.
*   **Персистентность данных:** Данные сохраняются в файловой базе H2 и доступны между перезапусками приложения.
*   **Бизнес-логика:** Реализованы ключевые функции, такие как:
    *   Добавление и удаление друзей (односторонняя связь).
    *   Постановка и удаление лайков фильмам.
    *   Получение списка самых популярных фильмов.
*   **Валидация:** Используются стандартные и кастомные аннотации для проверки корректности входящих данных.
*   **Интеграционное тестирование:** Написаны тесты для слоя DAO (`@JdbcTest`), проверяющие корректность взаимодействия с базой данных.

## Схема базы данных Filmorate

### ER-диаграмма

Ниже представлена диаграмма сущность-связь для базы данных проекта.

![ER-диаграмма базы данных Filmorate](DB_filmorate.png)

### Примеры SQL-запросов

**1. Получить топ-10 самых популярных фильмов:**
```sql
SELECT f.*, m.name as mpa_name
FROM films AS f
JOIN mpa AS m ON f.mpa_id = m.mpa_id
LEFT JOIN likes AS l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT 10;

**2. Получить всех друзей пользователя с ID=1:**
```sql
SELECT u.*
FROM users AS u
JOIN user_friends AS uf ON u.user_id = uf.friend_id
WHERE uf.user_id = 1;
```

**3. Найти общих друзей между пользователями с ID=1 и ID=2:**
```sql
SELECT u.*
FROM users AS u
JOIN user_friends AS uf1 ON u.user_id = uf1.friend_id
JOIN user_friends AS uf2 ON u.user_id = uf2.friend_id
WHERE uf1.user_id = 1 AND uf2.user_id = 2;
```

**4. Получить фильм с ID=1 со всеми его жанрами:**
```sql
-- Сначала получаем основную информацию о фильме
SELECT f.*, m.name as mpa_name
FROM films AS f
JOIN mpa AS m ON f.mpa_id = m.mpa_id
WHERE f.film_id = 1;

-- Затем получаем все его жанры
SELECT g.*
FROM genres AS g
JOIN film_genres AS fg ON g.genre_id = fg.genre_id
WHERE fg.film_id = 1;
```  

## Как запустить проект

1.  **Клонируйте репозиторий:**
    ```bash
    git clone https://github.com/bolshovaelizaveta/Filmorate.git
    cd Filmorate
    ```
2.  **Соберите проект с помощью Maven:**
   Эта команда скомпилирует код, запустит все тесты и упакует приложение в .jar файл.
    ```bash
    mvn clean package
    ```
4.  **Запустите Spring Boot приложение:**
    ```bash
    java -jar target/Filmorate-1.0-SNAPSHOT.jar
    ```
    Приложение будет доступно по умолчанию на `http://localhost:8080` (порт можно заменить в `application.properties`).
