package edu.tcu.cs.hogwartsartifactsonline.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for User APIs")
@Tag("integration")
class HogwartsUserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // john has all permissions
        ResultActions resultActions = mockMvc.perform(post("/auth/login").header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString("john:123456".getBytes())));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @Test
    @DisplayName("Check findAll (GET /)")
    void testFindAll() throws Exception {
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(("4")))
                .andExpect(jsonPath("$.data[0].username").value("john"));
    }

    @Test
    @DisplayName("Check findById (GET /{UserId})")
    void testFindById() throws Exception {
        mockMvc.perform(get("/users/5").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(("5")))
                .andExpect(jsonPath("$.data.username").value("eric"));
    }

    @Test
    @DisplayName("Check findById (GET /{UserId})")
    void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/8").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(("Could not find user with id 8 :(")));
    }

    @Test
    void testSaveWithValidInput() throws Exception {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setUsername("lily");
        hogwartsUser.setPassword("123456");
        hogwartsUser.setEnabled(true);
        hogwartsUser.setRoles("admin user"); // the delimiter is space

        String json = this.objectMapper.writeValueAsString(hogwartsUser);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Save Success")));
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    @Test
    void testUpdateFound() throws Exception {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setUsername("tom123");
        hogwartsUser.setPassword("654321");
        hogwartsUser.setEnabled(false);
        hogwartsUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(hogwartsUser);

        mockMvc.perform(put("/users/6").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Update Success")));
        mockMvc.perform(get("/users/6").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(("6")))
                .andExpect(jsonPath("$.data.username").value(("tom123")))
                .andExpect(jsonPath("$.data.enabled").value("false"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setUsername("john123");
        hogwartsUser.setEnabled(true);
        hogwartsUser.setRoles("admin user\t");

        String json = this.objectMapper.writeValueAsString(hogwartsUser);

        mockMvc.perform(put("/users/9").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(("Could not find user with id 9 :(")));
        mockMvc.perform(get("/users/4").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(("4")))
                .andExpect(jsonPath("$.data.username").value(("john")))
                .andExpect(jsonPath("$.data.enabled").value("true"))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/users/5").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Delete Success")));
        mockMvc.perform(get("/users/5").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(("Could not find user with id 5 :(")));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD) // reset H2 database before calling this test case
    void testDeleteNoAccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/auth/login").header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString("eric:654321".getBytes())));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        String ericToken = "Bearer " + json.getJSONObject("data").getString("token");

        mockMvc.perform(delete("/users/6").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, ericToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(("No permission.")));
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(("4")))
                .andExpect(jsonPath("$.data[0].username").value("john"));
    }

}
