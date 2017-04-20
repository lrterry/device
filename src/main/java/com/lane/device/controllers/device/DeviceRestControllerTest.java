package com.lane.device.controllers.device;

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
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * Created by lane on 3/20/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceApplication.class)
@WebAppConfiguration
public class DeviceRestControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String username = "testusername";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Account account;

    private List<Device> devices = new ArrayList<>();

    @Autowired
    private DeviceRepository deviceRepository;

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

        this.deviceRepository.deleteAllInBatch();
        this.accountRepository.deleteAllInBatch();

        this.account = accountRepository.save(new Account(username, "password"));
        this.devices.add(deviceRepository.save(new Device(account,  "test", "test", "test")));
        this.devices.add(deviceRepository.save(new Device(account, "test2", "test2", "test2")));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/asdasd/devices")
                .content(this.json(new Device()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleDevice() throws Exception {
        mockMvc.perform(get("/devices/" + this.devices.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.devices.get(0).getId().intValue())))
                .andExpect(jsonPath("$.name", is(this.devices.get(0).getName())))
                .andExpect(jsonPath("$.serial_number", is(this.devices.get(0).getSerialNumber())))
                .andExpect(jsonPath("$.platform", is(this.devices.get(0).getPlatform())));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}