# Бэкенд приложения для просмотра расписания
Frontend:
https://github.com/FADelto/MISIS-Y.P-Schedule-Frontend

Макет приложения:
https://www.figma.com/file/AdPtt8IgBkfQvuebVXsEMv/Schedule-app?type=design&node-id=0%3A1&mode=design&t=2rfRbSg3QKpedQyE-1

## Функции:
- Тестирование приложения и документация API приложения в Swagger 
- Получение расписания с Я.Пачки при помощи OpenAI Assistant
- Telegram бот для просмотра новостей и расписания
- Telegram бот для уведомления о начале пары

## Стек:
* Java 20
* Gradle
* Spring Boot 3.2.2
* Lombok 8.4
* Liquibase 4.25.1
* PostgreSQL 42.5.1
* Telegram Bots 6.9.7.1
* jjwt 0.12.5
* OkHttp 4.12.0
* Jackson 2.16.1

## TO-DO List:
 - [x] Получение расписания с API МИСИС
 - [ ] Обновление расписания в базе данных
 - [ ] Логика таск-менеджера
 - [x] Получение новостей с пачки
 - [ ] Создание документации к API в Swagger
 - [x]  Подготовка к контейнеризации приложения в Docker (Создание двух фазной сборки)
## Для запуска:
- Заполнить данные в docker-compose.yml или в application.yml
- Если запуск будет не через Docker, то создать базу данных и указать в application.yml данные для подключения
- Можно запустить через IDE или с помощью Docker

Для запуска при помощи Docker:
  
  Создать образ:
  ```docker compose build```
  
  Запуск приложения:
  ```docker compose up```
