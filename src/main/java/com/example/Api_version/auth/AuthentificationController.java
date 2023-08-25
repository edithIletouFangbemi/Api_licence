package com.example.Api_version.auth;

import com.example.Api_version.Services.AuthenticationService;
import com.example.Api_version.entities.User;
import com.example.Api_version.event.RegistrationCompletePublisherEvent;
import com.example.Api_version.request.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("utilisateur")
@RequiredArgsConstructor
@CrossOrigin
public class AuthentificationController {
    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final ApplicationEventPublisher publisher;
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request, final HttpServletRequest httpRequest
            ){
        User response = service.register(request);
        publisher.publishEvent(new RegistrationCompletePublisherEvent(response,applicationUrl(httpRequest)));
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(service.resetPassword(request));
    }
    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@RequestBody String request) throws MessagingException {
        return ResponseEntity.ok(service.forgotPassword(request));
    }

    @PostMapping("/authentication")
    public ResponseEntity<UserReturnRequest> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> liste(){
        return  ResponseEntity.ok(service.lister());
    }
    @GetMapping("/{code}")
    public ResponseEntity<User> getOne(@PathVariable("code") String code){
        return new ResponseEntity<User>(service.one(code), HttpStatus.OK);
    }
    @PutMapping("/{code}")
    public ResponseEntity<User> update(@PathVariable("code") String code, @RequestBody RegisterRequest request ){
        return new ResponseEntity<User>(service.update(code, request),HttpStatus.OK);
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<User> delete(@PathVariable("code") String code){
        return new ResponseEntity<User>(service.delete(code), HttpStatus.OK);
    }

    @DeleteMapping("/activer/{code}")
    public ResponseEntity<User> activer(@PathVariable("code") String code){
        return ResponseEntity.ok(service.activer(code));
    }

    public String applicationUrl(HttpServletRequest httpRequest) {
        return "http://"+httpRequest.getServerName()+":"
                +httpRequest.getServerPort()+httpRequest.getContextPath();
    }
}