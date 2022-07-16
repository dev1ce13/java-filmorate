# java-filmorate
Template repository for Filmorate project.

В проекте реализованы классы Film и User, а также созданы 
FilmController и UserController для работы с приложением
с помощью GET, POST и PUT запросов. Также создано 
собственное исключение ValidationException, которое 
используется при валидации данных.

В проект добавлены сервисы UserService и FilmService, а также
реализованы хранилища и интерфейс к ним. Создано новое исключение
NotFoundException и контроллер ErrorHandler, который отвечает
за обработку статус-кода исключений.
