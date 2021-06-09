package dev.boiarshinov.enumsinapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class EnumInApiControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper jsonMapper;

    @Test
    void getModelWithSimpleEnum() throws Exception {
        mockMvc.perform(get("/simple"))
            .andDo(print())
            .andExpect(jsonPath("$.dayOfWeek").value("MONDAY"));
    }

    @Test
    void withJsonValue() throws Exception {
        String inputJson = "{\"testId\":123,\"teacherId\":123,\"grade\":\"C\"}";

        mockMvc.perform(
            post("/jsonValue")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.grade").value("C"));
    }


}