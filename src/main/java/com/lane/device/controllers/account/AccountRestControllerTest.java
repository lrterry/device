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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private List<Account> accounts = new ArrayList<>();

    // TODO
    /*  Find out what is causing this issue. I believe it stems from populating
        the database in DeviceApplication and causes an indexing issue when working
        with the database on a local machine. When hosting a clean production database
        this issue should hopefully work itself out. Remember to deploy a development
        database as well.
    */

    private final static int buggedValue = 8;

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

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.accountRepository.deleteAllInBatch();
        for(Long i = Long.valueOf(0); i < 20; i++) {
            this.accounts.add(accountRepository.save(new Account("testing" + Long.toString(i), "password" + Long.toString(i))));
        }
    }

    @Test
    public void userIdNotFoundWhenDeleting() throws Exception {

        mockMvc.perform(delete("/users/" +  String.valueOf(accounts.size() + 1)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void userIdNotFoundWhenGettingUser() throws Exception {
        mockMvc.perform(get("/users/" + String.valueOf(accounts.size() + 5)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void correctUserFound() throws Exception {
        int randNum = ThreadLocalRandom.current().nextInt(0, accounts.size() + 1);
        mockMvc.perform(get("/users/" + String.valueOf(randNum + buggedValue)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.accounts.get(randNum).getId().intValue())))
                .andExpect(jsonPath("$.username", is(this.accounts.get(randNum).getUsername())))
                .andExpect(jsonPath("$.password", is(this.accounts.get(randNum).getPassword())));
    }

}