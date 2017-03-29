package com.lane.device.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lrterry on 3/28/17.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(Long id) {
        super("Could not find user with id " + Long.toString(id));
    }
}
