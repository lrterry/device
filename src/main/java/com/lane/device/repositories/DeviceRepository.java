package com.lane.device.repositories;

import com.lane.device.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created by lane on 3/20/17.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Collection<Device> findByAccountUsername(String username);
}
