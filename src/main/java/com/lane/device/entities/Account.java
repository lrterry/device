package com.lane.device.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lane on 3/20/17.
 */

@Entity
public class Account implements Serializable {

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Device> devices = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Set<Device> getDevices() {
        return devices;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public Account() {}
}
