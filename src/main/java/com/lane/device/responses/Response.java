package com.lane.device.responses;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lrterry on 3/28/17.
 */
@Entity
public class Response implements Serializable {

    @Id
    private Long id;

    private String message;
    private Date created;

    public Response(String username) {
        this.created = new Date();
        this.message = formatMessage(username, created);
    }

    private String formatMessage(String text, Date date) {
        return (text + " successfully created at " + date.toString());
    }

    public Response() {}

    public String getMessage() {
        return message;
    }

    public Date getCreated() {
        return created;
    }
}
