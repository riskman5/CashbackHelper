import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.babenko.Main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CardsIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = {"/AddValidSberbank.sql"})
    @Test
    public void testAddCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"Sberbank\", \"cardName\": \"Sberbank Visa\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bankName").value("Sberbank"))
                .andExpect(jsonPath("$.name").value("Sberbank Visa"))
                .andExpect(jsonPath("$.earnedCashbackAmount").isNumber());
    }

    @Test
    public void testAddCardWithUnknownBankName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"UnknownBank\", \"cardName\": \"Visa\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCardWithEmptyBankName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"\", \"cardName\": \"Visa\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddCardWithEmptyCardName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"Sberbank\", \"cardName\": \"\" }"))
                .andExpect(status().isBadRequest());
    }

    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql"})
    @Test
    public void testAddCardWithExistingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"Sberbank\", \"cardName\": \"Sberbank Visa\" }"))
                .andExpect(status().isConflict());
    }

    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql"})
    @Test
    public void testGetCards() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"bankName\": \"Sberbank\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bankName").value("Sberbank"))
                .andExpect(jsonPath("$[0].name").value("Sberbank Visa"));
    }

    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    @Test
    public void testTransactionWithCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cards/Sberbank Visa/transactions")
                        .param("category", "Развлечения")
                        .param("transactionAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.earnedCashbackAmount").value(20));
    }

    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    @Test
    public void testTransactionWithUnknownCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cards/Sberbank Visa/transactions")
                        .param("category", "UnknownCategory")
                        .param("transactionAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.earnedCashbackAmount").value(0));
    }

    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql",
            "/AddValidSberbankVisaCasback.sql", "/SpendAllCashbackAmountSberbankVisa.sql"})
    @Test
    public void testTransactionWithEmptyCashbackAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cards/Sberbank Visa/transactions")
                        .param("category", "Развлечения")
                        .param("transactionAmount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingCashbackAmount").value(0));
    }
}
