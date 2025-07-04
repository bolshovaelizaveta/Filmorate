# Filmorate

![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-green.svg?style=flat&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-blue.svg?style=flat&logo=apache-maven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-red.svg?style=flat&logo=lombok&logoColor=white)
![Jakarta Validation](https://img.shields.io/badge/Jakarta_Validation-blueviolet.svg?style=flat&logo=spring&logoColor=white)
![REST API](https://img.shields.io/badge/REST_API-lightgrey.svg?style=flat&logo=rest&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37.svg?style=flat&logo=postman&logoColor=white)

## Описание проекта

**Filmorate** — это бэкенд-сервис для платформы по управлению фильмами и пользовательскими оценками, разработанный на **Java** с использованием **Spring Boot**. Проект предназначен для демонстрации построения **RESTful API** и работы с бизнес-логикой, связанной с фильмами, пользователями и их рекомендациями.

![Пример работы Filmorate](https://code.s3.yandex.net/Java/Peresborka/10project/Comp-1-3.gif?etag=fdcd9ede1ee1bce2f751a297aa99c6a3)

Главная цель сервиса — помочь пользователям и их друзьям быстро находить интересные фильмы для просмотра, предоставляя рекомендации и топы лучших фильмов.

## Текущие задачи спринта

В рамках текущего спринта реализован **каркас Spring Boot приложения**, который закладывает основу для дальнейшего развития сервиса. Это включает в себя:

* Базовую структуру проекта.
* Настройку основных зависимостей.
* Готовность к приему и обработке HTTP-запросов.

## Используемые технологии

* **Java 11+**
* **Spring Boot** (используется версия 3.2.2)
* **Maven** (для управления сборкой и зависимостями)
* **Lombok** (для сокращения шаблонного кода моделей)
* **In-Memory Storage** (для временного хранения данных в коллекциях `HashMap` на данном этапе)

## Демонстрируемые возможности и концепции (после завершения спринта)

После завершения текущего и последующих спринтов, проект будет демонстрировать:

* **RESTful API Design:** Проектирование эндпоинтов для ресурсов `Movie` (фильмы), `User` (пользователи), `Rating` (оценки).
* **Spring Boot Controllers:** Обработка HTTP-запросов с использованием `@RestController`.
* **Валидация данных:** Применение бизнес-правил и проверка входящих данных.
* **Пользовательские исключения:** Обработка специфичных сценариев ошибок.
* **Взаимодействие между ресурсами:** Реализация логики для связывания пользователей с их оценками и фильмами.
* **Бизнес-логика рекомендаций:** Возвращение топ-фильмов, рекомендованных к просмотру.
* **In-Memory Data Storage:** Временное хранение данных (на начальных этапах).

## Как запустить проект

1.  **Клонируйте репозиторий:**
    ```bash
    git clone https://github.com/bolshovaelizaveta/Filmorate.git
    cd Filmorate
    ```
2.  **Соберите проект с помощью Maven:**
    ```bash
    mvn clean install
    ```
3.  **Запустите Spring Boot приложение:**
    ```bash
    mvn spring-boot:run
    ```
    Приложение будет доступно по умолчанию на `http://localhost:8080` (порт можно заменить в `application.properties`).
