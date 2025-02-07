package com.urlshortener.redirect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimiterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRateLimiting() throws Exception {

        String url = "http://localhost:8083/api/redirect/OOk68TfA";

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get(url))
                   .andExpect(status().is3xxRedirection());
            Thread.sleep(100);
        }


        mockMvc.perform(get(url))
               .andExpect(status().isTooManyRequests());
    }
}
