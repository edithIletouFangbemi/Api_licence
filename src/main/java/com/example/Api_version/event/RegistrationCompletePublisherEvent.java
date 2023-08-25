package com.example.Api_version.event;

import com.example.Api_version.entities.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class RegistrationCompletePublisherEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;


    public RegistrationCompletePublisherEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
