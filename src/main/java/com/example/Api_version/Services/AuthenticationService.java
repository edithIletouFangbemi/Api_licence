package com.example.Api_version.Services;

import com.example.Api_version.entities.Body;
import com.example.Api_version.entities.Profil;
import com.example.Api_version.entities.User;
import com.example.Api_version.event.token.VerificationToken;
import com.example.Api_version.event.token.VerificationTokenRepository;
import com.example.Api_version.exceptions.ProduitException;
import com.example.Api_version.exceptions.UserException;
import com.example.Api_version.repositories.ProfilRepository;
import com.example.Api_version.repositories.UserRepository;
import com.example.Api_version.request.*;
import com.example.Api_version.utils.CodeGenerator;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
@Transactional
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ProfilRepository profilRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final JwtService jwtService;
    private VerificationTokenRepository tokenRepository;
    private User utilisateur;
    private Body body;
    private Profil profil;
    private VerificationToken verificationToken;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder,
                                 ProfilRepository profilRepository, VerificationTokenRepository verificationTokenRepository, EmailSenderService emailSenderService, JwtService jwtService, AuthenticationManager authenticationManager,
                                 VerificationTokenRepository tokenRepository
                                 ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.profilRepository = profilRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    /**
     * création d'un utilisateur et envoie de mail
     * @param request
     * @return l'utilisateur créé
     */
    public User register(RegisterRequest request) {
        Optional<User> userOptionalbyLastname = repository.findByLastnameAndFirstnameAndEmail(request.getLastname(),
                request.getFirstname(),request.getEmail());

        Optional<User> userOptionalByEmail = repository.findByEmailIgnoreCase(request.getEmail());

        if(userOptionalbyLastname.isPresent()){
            var user2 = userOptionalbyLastname.get();
            if(user2.getStatut() == 1) throw new UserException("Un utilisateur existe deja avec ces informations!! ", HttpStatus.CONFLICT);
            else{
                user2.setStatut(1);
                repository.save(user2);
            }
        }
        if(userOptionalByEmail.isPresent() && userOptionalByEmail.get().getStatut() == 1 ) throw new UserException("cet adresse email est deja utilisé donc changez le !!", HttpStatus.CONFLICT);

        Optional<Profil> profilOptionnel = profilRepository.findByCodeProfil(request.getRole());

        if(profilOptionnel.isEmpty() || profilOptionnel.get().getStatut() == 2) throw new UserException("ce profil n'existe pas!!", HttpStatus.NOT_FOUND);
        profil = profilOptionnel.get();
        var user = User.builder()
                .codeUser(CodeGenerator.codeUser(request.getLastname(),request.getFirstname()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .statut(1)
                .enabled(false)
                .password(CodeGenerator.passwordCode(request.getLastname(), request.getFirstname()))
               // .password(CodeGenerator.passwordCode(request.getLastname(), request.getFirstname()))
                .profil(profil)
                .build();
        return repository.save(user);

    }

    /**
     * Authentification de l'utilisateur
     * @param request
     * @return
     */
    public UserReturnRequest authenticate(AuthenticationRequest request) {
         authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

         var user = repository.findByEmail(request.getEmail()).orElseThrow();
         if(user == null) throw new ProduitException("email introuvable!!");
         if(user.isEnabled()== false) throw new ProduitException("Impossible de vous connecter veuillez verifier si votre compte est inactif consulter vos mails!! ");
         var jwtToken = jwtService.generateToken(user);
         var reponse = UserReturnRequest.builder(
                ).codeUser(user.getCodeUser())
                .profil(user.getProfil())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .email(user.getEmail())
                .statut(user.getStatut())
                .token(jwtToken)
                .build();


        return reponse;
         /*
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();*/

    }

    public User resetPassword(ResetPasswordRequest request){
        Optional<User> userOptional = repository.findByEmail(request.getEmail());
        utilisateur = userOptional.get();
        if(userOptional.isEmpty() || utilisateur.getStatut() == 2) throw new ProduitException("aucun compte avec ces informations!");
        //Optional<VerificationToken> tokenOptional = verificationTokenRepository.findByUser(utilisateur);

        //if(tokenOptional.isEmpty()) throw new ProduitException("aucun token ne correspond à cet utilisateur!!");
      //  verificationToken = tokenOptional.get();
       // if(!request.getVerificationToken().equals(verificationToken.getToken())) throw new ProduitException("votre token n'est pas valide reverifiez!");
      //  LocalDateTime expirationDateTime = verificationToken.getExpirationTime()
              //  .toInstant()
               // .atZone(ZoneId.systemDefault())
               // .toLocalDateTime();
       // if(LocalDateTime.now().isAfter(expirationDateTime)) throw new ProduitException("votre token a expiré");
        //comparer l'ancien mot de passe à ce qu'il ya dans la base
        if(!utilisateur.getPassword().equals(request.getOldPassword())) throw new ProduitException("l'ancien mot de passe fourni est incorrecte!!");
        utilisateur.setPassword(passwordEncoder.encode(request.getNewPassword()));
        utilisateur.setEnabled(true);
        // changer isEnabled à true

        // enregistre le user dans la bd

       return repository.save(utilisateur);

    }

    public String forgotPassword(String email) throws MessagingException {
        Optional<User> userOptional = repository.findByEmail(email);
        if(userOptional.isEmpty()) throw new ProduitException("aucun utilisateur avec ce mail");
        if(!userOptional.get().isEnabled()) throw new ProduitException("votre compte n'est pas encore actif!!");
        utilisateur = userOptional.get();
        body = Body.builder()
                .url("http://localhost:4200/resetPassword")
                .build();
        emailSenderService.sendEmail(utilisateur.getEmail(), "cliquer pour Changer votre mot de passe","http://localhost:4200/resetPassword");
        return "un mail vous est envoyé allez verifier!";
    }

    public List<User> lister(){
        return repository.findAll();
    }

    public User one(String code){
        Optional<User> utilisateurOptional = repository.findByCodeUser(code);
        if(utilisateurOptional.isEmpty() || utilisateurOptional.get().getStatut() ==2) throw new ProduitException("aucun utilisateur n'existe avec ce code d'identification "+ code);
        return utilisateurOptional.get();
    }

    public User update(String code, RegisterRequest request){
        Optional<User> utilisateurOptional = repository.findByCodeUser(code);
        Optional<Profil> profilOptionnel = profilRepository.findByLibelle(request.getRole());

        if(utilisateurOptional.isEmpty() || utilisateurOptional.get().getStatut() ==2) throw new ProduitException("aucun utilisateur n'existe avec ce code d'identification "+ code);
        if(profilOptionnel.isEmpty() || profilOptionnel.get().getStatut()== 2) throw new ProduitException("ce profil n'existe pas!!");

        utilisateur = utilisateurOptional.get();

        profil = profilOptionnel.get();
        utilisateur.setEmail(request.getEmail());
        utilisateur.setLastname(request.getLastname());
        utilisateur.setFirstname(request.getFirstname());
        utilisateur.setProfil(profil);

        return repository.save(utilisateur);


    }

    public User delete(String code){
        Optional<User> utilisateurOptional = repository.findByCodeUser(code);
        if(utilisateurOptional.isEmpty() || utilisateurOptional.get().getStatut() ==2) throw new ProduitException("aucun utilisateur n'existe avec ce code d'identification "+ code);
        utilisateur = utilisateurOptional.get();

        utilisateur.setStatut(2);

        return repository.save(utilisateur);


    }

    public User activer(String code){
        Optional<User> utilisateurOptional = repository.findByCodeUser(code);
        if(utilisateurOptional.isEmpty()) throw new ProduitException("aucun utilisateur n'existe avec ce code d'identification "+ code);
        utilisateur = utilisateurOptional.get();
        if(utilisateur.getStatut() ==1) throw new ProduitException("l'utilisateur déjà actif");

        utilisateur.setStatut(1);
       return repository.save(utilisateur);

    }

    public void saveUserVerificationToken(User registeredUser, String token) {
        var verificationToken = new VerificationToken(token,registeredUser);
        tokenRepository.save(verificationToken);
    }

    public Principal getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Principal) authentication.getPrincipal();
    }
}
