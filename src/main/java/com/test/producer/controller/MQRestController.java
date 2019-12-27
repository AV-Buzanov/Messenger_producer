package com.test.producer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс реализует рест контроллер для взаимодействия с очередью.
 * @author Aleksey Buzanov
 */
@RestController
@RequestMapping(value = "/message")
public class MQRestController{
    private final JmsTemplate jmsTemplate;

    @Value("${spring.activemq.queue}")
    private String queue;

    public MQRestController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Метод добавляет в очередь переданное сообщение, используя jmsTemplate
     * @param message строка размером до 255 символов с текстом отправляемого сообщения.
     * @return ResponseEntity с текстом "Сообщение отправлено" в случае успешной отправки,
     * "Queue error" при возникновении JmsException
     */
    @PostMapping(value = "/send", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sendMessage(@RequestBody(required = true) final String message) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        try {
            jmsTemplate.convertAndSend(queue, message);
            return new ResponseEntity<String>("Сообщение отправлено", responseHeaders, HttpStatus.OK);
        } catch (JmsException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Queue error", responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
