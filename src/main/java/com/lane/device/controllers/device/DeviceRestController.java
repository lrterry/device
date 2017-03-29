package com.lane.device.controllers.device;

import com.lane.device.entities.Device;
import com.lane.device.exceptions.UserNotFoundException;
import com.lane.device.repositories.AccountRepository;
import com.lane.device.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;


/**
 * Created by lane on 3/20/17.
 */

@RestController
@RequestMapping("/devices")
public class DeviceRestController {

    private final DeviceRepository deviceRepository;
    private final AccountRepository accountRepository;

    @Autowired
    DeviceRestController(DeviceRepository deviceRepository, AccountRepository accountRepository) {
        this.deviceRepository = deviceRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Device> getDevices() {
        return this.deviceRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{username}")
    Collection<Device> getUserDevices(@PathVariable String username) {
        return this.deviceRepository.findByAccountUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> addDevice(@RequestBody Device device) {
        Device result = deviceRepository.save(device);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{/id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{deviceId}")
    Device getUserDevice(@PathVariable Long deviceId) {
        return this.deviceRepository.findOne(deviceId);
    }

    private void validateUser(String username) {
        this.accountRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username));
    }

}