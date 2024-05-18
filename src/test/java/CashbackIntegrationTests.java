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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CashbackIntegrationTests {
@Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql"})
    public void testAddCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cashback/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardName\": \"Sberbank Visa\", \"category\": \"Развлечения\", \"cashbackPercentage\": 5 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Развлечения"))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now().withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusMonths(1).withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.cashbackPercentage").value(5))
                .andExpect(jsonPath("$.isPermanent").value(false));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql"})
    public void testAddCashbackWithPermanent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cashback/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardName\": \"Sberbank Visa\", \"category\": \"Развлечения\", \"cashbackPercentage\": 5, \"isPermanent\": true }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Развлечения"))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now().withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.endDate").isEmpty())
                .andExpect(jsonPath("$.cashbackPercentage").value(5))
                .andExpect(jsonPath("$.isPermanent").value(true));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    public void testAddCashbackWithExistingCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cashback/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardName\": \"Sberbank Visa\", \"category\": \"Развлечения\", \"cashbackPercentage\": 5 }"))
                .andExpect(status().isConflict());
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql"})
    public void testAddFutureCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cashback/future")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardName\": \"Sberbank Visa\", \"category\": \"Развлечения\", \"cashbackPercentage\": 5 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Развлечения"))
                .andExpect(jsonPath("$.startDate").value(LocalDate.now().plusMonths(1).withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusMonths(2).withDayOfMonth(1).toString()))
                .andExpect(jsonPath("$.cashbackPercentage").value(5))
                .andExpect(jsonPath("$.isPermanent").value(false));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    public void testExpireCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cashback/expire"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    public void testDeleteCurrentCashbackAndAddAgain() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cashback/current")
                        .param("cardName", "Sberbank Visa")
                        .param("category", "Развлечения"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cashback/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cardName\": \"Sberbank Visa\", \"category\": \"Развлечения\", \"cashbackPercentage\": 5 }"))
                .andExpect(status().isOk());
    }
}
