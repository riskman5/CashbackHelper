import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.babenko.Main;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChooseCardIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNoCards() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Развлечения"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql"})
    public void testChooseCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Развлечения")
                        .param("value", "1000"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("Sberbank Visa"));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql",
            "/AddValidTinkoff.sql", "/AddValidTinkoffCard.sql", "/AddValidTinkoffMirCashback.sql"})
    public void testChooseCardWithMoreCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Развлечения")
                        .param("value", "1000"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("Tinkoff Mir"));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql",
                    "/AddValidTinkoff.sql", "/AddValidTinkoffCard.sql", "/AddValidTinkoffMirCashback.sql",
                    "/SpendAllCashbackAmountTinkoffMir.sql"})
    public void testChooseCardWithMoreWithMoreCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Развлечения")
                        .param("value", "500"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("Sberbank Visa"));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql",
                    "/AddValidTinkoff.sql", "/AddValidTinkoffCard.sql", "/AddValidTinkoffMirCashback.sql",
                    "/SpendAllCashbackAmountTinkoffMir.sql", "/SpendAllCashbackAmountSberbankVisa.sql"})
    public void testChooseCardWithNoCashback() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Развлечения")
                        .param("value", "500"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Tinkoff Mir")
                        || result.getResponse().getContentAsString().contains("Sberbank Visa")));
    }

    @Test
    @Sql(scripts = {"/AddValidSberbank.sql", "/AddValidSberbankCard.sql", "/AddValidSberbankVisaCasback.sql",
                    "/AddValidTinkoff.sql", "/AddValidTinkoffCard.sql", "/AddValidTinkoffMirCashback.sql",  "/SpendAllCashbackAmountSberbankVisa.sql"})
    public void testChooseCardWithNoCashbackToCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/choose")
                        .param("category", "Спорт")
                        .param("value", "500"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Tinkoff Mir")
                        || result.getResponse().getContentAsString().contains("Sberbank Visa")));
    }
}