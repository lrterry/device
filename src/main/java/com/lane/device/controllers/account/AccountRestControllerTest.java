package com.lane.device.controllers.account;

import com.lane.device.DeviceApplication;
import com.lane.device.entities.Account;
import com.lane.device.entities.Device;
import com.lane.device.repositories.AccountRepository;
import com.lane.device.repositories.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;



import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by lrterry on 3/24/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceApplication.class)
@WebAppConfiguration
public class AccountRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Account account;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
        assertNotNull("The JSON message converter should not be null", this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        String username = "testerino";
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.accountRepository.deleteAllInBatch();

        this.account = accountRepository.save(new Account(username, "password"));
    }

    @Test
    public void userIdNotFoundWhenDeleting() throws Exception {
        mockMvc.perform(delete("/users/45"))
                .andExpect(status().isNotFound());

    }
}
