package com.lane.device.controllers.account;

import com.lane.device.entities.Account;
import com.lane.device.exceptions.UserIdNotFoundException;
import com.lane.device.repositories.AccountRepository;
import com.lane.device.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;

/**
 * Created by lane on 3/20/17.
 */
@RestController
@RequestMapping("/users")
public class AccountRestController {

    private final AccountRepository accountRepository;

    @Autowired
    AccountRestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody Account input, UriComponentsBuilder ucb) {
        Account result = accountRepository.save(new Account(input.getUsername(), input.getPassword()));

        return new ResponseEntity<Response>(new Response(result.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id) {
        validateUser(id);

        Account accountToDelete = accountRepository.findOne(id);
        accountRepository.delete(accountToDelete);
        return new ResponseEntity<Response>(new Response(accountToDelete.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    private void validateUser(Long id) {
        accountRepository.findById(id).orElseThrow(
                () -> new UserIdNotFoundException(id)
        );
    }

}