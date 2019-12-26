## Messenger producer
Приложение публикует REST сервис для взаимодействия с очередью.
Для отправки сообщения в очередь нужно отправить POST запрос на адресс http://localhost:8181/message/send/, добавив текст сообщения в тело запроса с типом text/plain. Страница с формой отправки сообщения располагается в директории [webapp](./src/main/webapp) 

## Commands to build project
```
mvn clean
mvn install
```
## Commands to start application
```
java -jar task-manager.jar
```

## Стек технологий

* Java 1.8
* Maven 3.6.0 
* Spring Boot 2.2.2.RELEASE
