package com.example.Api_version.event.listener;

import com.example.Api_version.Services.AuthenticationService;
import com.example.Api_version.Services.EmailSenderService;
import com.example.Api_version.entities.Body;
import com.example.Api_version.entities.User;
import com.example.Api_version.event.RegistrationCompletePublisherEvent;
import com.example.Api_version.exceptions.ProduitException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;



@Component
@RequiredArgsConstructor
public class RegistrationCompletePublisherEventListener implements
        ApplicationListener<RegistrationCompletePublisherEvent> {
    private final AuthenticationService userService;

    @Autowired
    private EmailSenderService emailSenderService;
    @Override
    public void onApplicationEvent(RegistrationCompletePublisherEvent event) {
        //1. get the new registered user
        User registeredUser = event.getUser();
        String url = "http://localhost:4200/resetPassword";

        if(registeredUser == null) throw new ProduitException("les informations de cet utilisateur sont null");
        //2. generate a verification token
        String verificationToken = UUID.randomUUID().toString();
        String content = "Bienvenu sur la plateforme de Génération de Licence!<br><br>"
                +"Mot de passe : "+ registeredUser.getPassword()+ "<br>"
                +"Token : "+ verificationToken+ "<br>"
                + "Cliquez sur le lien ci-dessous pour acceder a la plateforme:<br>"
                + "<a href='http://localhost:4200/resetPassword'>Acceder a la plateforme</a>";

        //3. save the verification token
        if(registeredUser.getEmail() == null) throw new ProduitException("le mail est null");
        userService.saveUserVerificationToken(registeredUser,verificationToken);
        //4. build the url to send to the user
        //String url = event.getApplicationUrl()+"/utilisateur/verifyEmail?token="+verificationToken;
        //5. send the email
        System.out.println(url);
        try {
            emailSenderService.sendEmail(registeredUser.getEmail(), "Changer Mot de Passe"
                    +registeredUser.getLastname()+" "+registeredUser.getFirstname(),content);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

}
