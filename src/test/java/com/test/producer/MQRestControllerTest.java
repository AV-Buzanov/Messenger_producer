package com.test.producer;

import com.test.producer.controller.MQRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MQRestController.class)
public class MQRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Test
    public void sendMessageTest() throws Exception {
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.TEXT_PLAIN).content("Message"))
                .andExpect(status().isOk()).andExpect(content().string("Сообщение отправлено"));
        verify(jmsTemplate, only()).convertAndSend(anyString(), anyString());
    }

    @Test
    public void sendMessage3TimesTest() throws Exception {
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.TEXT_PLAIN).content("Message 1"))
                .andExpect(status().isOk()).andExpect(content().string("Сообщение отправлено"));
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.TEXT_PLAIN).content("Сообщение 2"))
                .andExpect(status().isOk()).andExpect(content().string("Сообщение отправлено"));
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.TEXT_PLAIN).content(" "))
                .andExpect(status().isOk()).andExpect(content().string("Сообщение отправлено"));
        verify(jmsTemplate, times(3)).convertAndSend(anyString(), anyString());
    }

    @Test
    public void sendMessageWrongTypeTest() throws Exception {
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.APPLICATION_JSON).content("Message"))
                .andExpect(status().isUnsupportedMediaType());
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    public void sendMessageWithoutBodyTest() throws Exception {
        mockMvc.perform(post("/message/send")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    public void sendMessageWrongPathTest() throws Exception {
        mockMvc.perform(post("/message")
                .contentType(MediaType.TEXT_PLAIN).content("Message"))
                .andExpect(status().isNotFound());
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }
}
