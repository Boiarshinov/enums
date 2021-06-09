package dev.boiarshinov.enumsinapi.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest
public class OpenApiGeneratorTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void generateOpenApi() throws Exception {
        String openApiYaml = mockMvc.perform(get("/v3/api-docs.yaml"))
//            .andDo(print())
            .andReturn().getResponse().getContentAsString();

        System.out.println(openApiYaml);
    }
}
