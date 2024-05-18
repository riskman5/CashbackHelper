import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.babenko.Main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BanksIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddBank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Sberbank\", \"cashbackLimit\": 1000 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sberbank"))
                .andExpect(jsonPath("$.cashbackLimit").value(1000))
                .andExpect(jsonPath("$.cards").isEmpty());
    }

    @Test
    public void testAddBankWithNullCashbackLimit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Sberbank\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sberbank"))
                .andExpect(jsonPath("$.cashbackLimit").isEmpty())
                .andExpect(jsonPath("$.cards").isEmpty());
    }

    @Test
    public void testAddBankWithNegativeCashbackLimit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Sberbank\", \"cashbackLimit\": -1000 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddBankWithEmptyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"\", \"cashbackLimit\": 1000 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddBankWithExistingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Sberbank\", \"cashbackLimit\": 1000 }"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Sberbank\", \"cashbackLimit\": 1000 }"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testBankWithTooLongName() throws Exception {
        var tooLongName = "a".repeat(51);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"" + tooLongName + "\", \"cashbackLimit\": 1000 }"))
                .andExpect(status().isBadRequest());
    }
}
