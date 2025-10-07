# Менеджер задач 📝

### Полноценная веб-система для управления задачами, назначения исполнителей и смены статусов.

## 📈 Tests and linter status:
[![Actions Status](https://github.com/Alina-Zhdanova/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/Alina-Zhdanova/java-project-99/actions)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Alina-Zhdanova_java-project-99&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Alina-Zhdanova_java-project-99)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Alina-Zhdanova_java-project-99&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Alina-Zhdanova_java-project-99)

## 💻 Стек

![](https://img.shields.io/badge/Java-007396?style=flat&logo=java&logoColor=white)
![](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring&logoColor=white)
![](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white)
![](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)
![](https://img.shields.io/badge/Sentry-362D59?style=flat&logo=sentry&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/REST_API-000000?style=flat&logo=jsonplaceholder&logoColor=white)

## 💡 Ключевые возможности и архитектурные решения

Проект демонстрирует навыки работы с **ORM (Hibernate/JPA)**, сложными связями в базе данных (**O2M, M2M**) и полным циклом разработки веб-приложения.

* **REST API и MVC:** Реализована Backend-часть веб-приложения с использованием фреймворка Spring и **MVC-архитектуры**.
* **Управление данными:** Разработана логика фильтрации данных, унифицированы **CRUD-операции** с использованием ресурсного роутинга.
* **Сложные связи в БД:** Спроектированы модели данных и реализованы связи между сущностями (Задачи, Исполнители, Статусы) с помощью ORM.
* **Безопасность:** Внедрен механизм **Аутентификации и Авторизации** для контроля доступа к ресурсам.
* **Эксплуатация и мониторинг:** Интегрированы инструменты для продакшена: **Swagger** (документация API) и **Sentry** (сбор и отслеживание ошибок в режиме реального времени).
* **Тестирование:** Код покрыт **интеграционными тестами** для проверки ключевого функционала.

## ⚙️ Инструкция по локальному запуску

Для локальной работы с проектом требуется **Java 21+** и **Gradle**.

### 1. Подготовка и клонирование

1.  **Клонируйте репозиторий** и перейдите в папку проекта:
    ```bash
    git clone https://github.com/Alina-Zhdanova/java-project-99.git
    cd java-project-99
    ```

#### 2. Запуск приложения

1.  Убедитесь, что ваш файл конфигурации настроен для активации профиля с H2.
2.  Используйте **Gradle Application Plugin** для сборки и запуска:
    ```bash
    ./gradlew run
    ```

#### 3. Доступ к приложению и API

* **Веб-интерфейс:** `http://localhost:8080`
* **Документация API (Swagger UI):** `http://localhost:8080/swagger-ui.html`