package com.tourism.travels.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private Pojo mock;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {

        var testController = new TestController(mock);

        mockMvc = MockMvcBuilders.standaloneSetup(testController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void methodArgumentNotValidException() throws Exception {
        // Arrange
        var json = objectMapper.writeValueAsString(new Pojo());

        // Act/Assert
        mockMvc.perform(post("/test")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alreadyExistsException() throws Exception {
        // Arrange
        var json = createJson();

        when(mock.foo()).thenThrow(new AlreadyExistsException());

        // Act/Assert
        mockMvc.perform(post("/test")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundException() throws Exception {
        // Arrange
        var json = createJson();

        when(mock.foo()).thenThrow(new NotFoundException());

        // Act/Assert
        mockMvc.perform(post("/test")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void httpMessageNotReadableException() throws Exception {
        // Arrange
        var pojo = new Pojo();
        pojo.setFoo("foo");
        pojo.setStatus(Status.OPEN);

        var json = objectMapper.writeValueAsString(pojo).replace("OPEN", "FOO");

        // Act/Assert
        mockMvc.perform(post("/test")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void httpMessageNotReadableException2() throws Exception {
        // Arrange/Act/Assert
        mockMvc.perform(post("/test")
                        .content("}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void httpRequestMethodNotSupportedException() throws Exception {
        // Arrange/Act/Assert
        mockMvc.perform(delete("/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exception() throws Exception {
        // Arrange
        var json = createJson();

        when(mock.foo()).thenThrow(new RuntimeException());

        // Act/Assert
        mockMvc.perform(post("/test")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private String createJson() throws JsonProcessingException {

        var pojo = new Pojo();
        pojo.setFoo("foo");

        return objectMapper.writeValueAsString(pojo);
    }

}
