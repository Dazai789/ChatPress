package com.chatpress.v1.artifact;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createArtifact() throws Exception {
        mockMvc.perform(post("/api/artifacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Java Notes",
                                  "slug": "java-notes",
                                  "sourceContent": "# Java Notes"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Java Notes"))
                .andExpect(jsonPath("$.slug").value("java-notes"))
                .andExpect(jsonPath("$.renderedHtml").value("<h1>Java Notes</h1>\n"));
    }
}
