package com.example.Api_version.auth;

import com.example.Api_version.Services.AuthenticationService;
import com.example.Api_version.entities.User;
import com.example.Api_version.event.RegistrationCompletePublisherEvent;
import com.example.Api_version.request.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlleur de Gestion des utilisateurs
 */
@RestController
@RequestMapping("utilisateur")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Gestion des utilisateurs")
//hide a controller
@Hidden
public class AuthentificationController {
    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final ApplicationEventPublisher publisher;

    /**
     *
     * @param request
     * @param httpRequest
     * @return envoie un mail à l'utilisateur pour que cet utilisateur puisse changer de mot de passe et se connecter
     */
    @Operation(
            description = "api endpoint for swagger",
            summary = "this is a summary for swagger endpoint",
            responses = {
                    @ApiResponse (
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "UnAuthorized / invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequest request, final HttpServletRequest httpRequest
            ){
       // publisher.publishEvent(new RegistrationCompletePublisherEvent(response,applicationUrl(httpRequest)));
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/changer")
    public ResponseEntity<User> reset(@Valid @RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(service.resetPassword(request));
    }
    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@Valid @RequestBody String request) throws MessagingException {
        return ResponseEntity.ok(service.forgotPassword(request));
    }
    //hide a mapping in swagger
    @Hidden
    @PostMapping("/authentication")
    public ResponseEntity<UserReturnRequest> authenticate(
            @Valid @RequestBody AuthenticationRequest request
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
        return ResponseEntity.ok(service.activerUtilisateur(code));
    }

    @DeleteMapping("/deactiver/{code}")
    public ResponseEntity<User> deactiver(@PathVariable("code") String code){
        return ResponseEntity.ok(service.deactiverUtilisateur(code));
    }

    public String applicationUrl(HttpServletRequest httpRequest) {
        return "http://"+httpRequest.getServerName()+":"
                +httpRequest.getServerPort()+httpRequest.getContextPath();
    }
}