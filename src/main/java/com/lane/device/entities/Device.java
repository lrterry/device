package com.lane.device.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by lane on 3/20/17.
 */

@Entity
public class Device implements Serializable {


    @JsonIgnore
    @ManyToOne
    private Account account;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("serial_number")
    public String serialNumber;

    public String name;
    public String platform;

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public String getPlatform() {
        return platform;
    }

    public Device(Account account, String serialNumber, String name, String platform) {
        this.account = account;
        this.serialNumber = serialNumber;
        this.name = name;
        this.platform = platform;
    }

    public Device() {}
}
