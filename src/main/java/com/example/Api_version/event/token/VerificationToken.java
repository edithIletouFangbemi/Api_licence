package com.example.Api_version.event.token;

import com.example.Api_version.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 15;

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    private Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
