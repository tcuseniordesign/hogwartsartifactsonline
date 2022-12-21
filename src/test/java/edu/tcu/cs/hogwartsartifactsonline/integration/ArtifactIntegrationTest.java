package edu.tcu.cs.hogwartsartifactsonline.integration;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Base64Utils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Artifact APIs")
@Tag("integration")
class ArtifactIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
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
        mockMvc.perform(get("/artifacts").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].id").value(("1250808601744904191")))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"));
    }

    @Test
    @DisplayName("Check findById (GET /{artifactId})")
    void testFindById() throws Exception {
        mockMvc.perform(get("/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(("1250808601744904191")))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testSaveWithValidInput() throws Exception {
        Artifact a = new Artifact();
        a.setName("New Artifact");
        a.setDescription("Description...");
        a.setImageUrl("imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        mockMvc.perform(post("/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Save Success")));
        mockMvc.perform(get("/artifacts").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(7)));
    }

    // I don't think it is necessary to test invalid input since this is a feature from the framework
    @Test
    void testSaveWithInvalidArtifactName() throws Exception {
        Artifact a = new Artifact();
        a.setName("");
        a.setDescription("Descriptions");
        a.setImageUrl("imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        mockMvc.perform(post("/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(("Provided arguments are invalid, see data for details.")))
                .andExpect(jsonPath("$.data.name").value(("name is required")));
        mockMvc.perform(get("/artifacts").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(6)));
    }

    // I don't think it is necessary to test invalid input since this is a feature from the framework
    @Test
    void testSaveWithInvalidArtifactDescription() throws Exception {
        Artifact a = new Artifact();
        a.setName("New Artifact");
        a.setDescription("");
        a.setImageUrl("imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        mockMvc.perform(post("/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(("Provided arguments are invalid, see data for details.")))
                .andExpect(jsonPath("$.data.description").value(("description is required")));
        mockMvc.perform(get("/artifacts").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data", hasSize(6)));
    }

    @Test
    void testUpdate() throws Exception {
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Artifact");
        a.setDescription("New description!");
        a.setImageUrl("imageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        mockMvc.perform(put("/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Update Success")));
        mockMvc.perform(get("/artifacts/1250808601744904192").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(("1250808601744904192")))
                .andExpect(jsonPath("$.data.description").value("New description!"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(("Delete Success")));
        mockMvc.perform(get("/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(("Could not find artifact with id 1250808601744904191 :(")));
    }

}
