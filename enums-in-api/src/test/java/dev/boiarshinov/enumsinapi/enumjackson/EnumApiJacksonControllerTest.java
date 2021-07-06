package dev.boiarshinov.enumsinapi.enumjackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnumApiJacksonControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper jsonMapper;

    @Test
    void getSchool() throws Exception {
        mockMvc.perform(get("/jackson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("ELEMENTARY"));
    }

    @Test
    void createSchool() throws Exception {
        School school = School.builder()
                .address("Rostov, Major Jukov, 5")
                .number("15 olympic reserve")
                .type(School.SchoolType.HIGH)
                .build();

        mockMvc.perform(
                post("/jackson")
                        .content(jsonMapper.writeValueAsString(school))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}