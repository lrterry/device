package com.lane.device;

import com.lane.device.entities.Account;
import com.lane.device.entities.Device;
import com.lane.device.repositories.AccountRepository;
import com.lane.device.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class DeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(AccountRepository accountRepository,
						   DeviceRepository deviceRepository) {
		return (evt) -> Arrays.asList(
				"test,test1,test2,test3,test4,test5,test6".split(","))
				.forEach(
						a -> {
							Account account = accountRepository.save(new Account(a, "password"));
						});
	}
}
