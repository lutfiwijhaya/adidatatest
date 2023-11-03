package com.lutfi.adidatatest.registration;


import com.lutfi.adidatatest.User.User;
import com.lutfi.adidatatest.User.UserService;
import com.lutfi.adidatatest.event.RegistrationCompleteEvent;
import com.lutfi.adidatatest.event.listener.RegistrationCompleteEventListener;
import com.lutfi.adidatatest.exception.UserAlreadyExistsException;
import com.lutfi.adidatatest.registration.password.PasswordResetRequest;
import com.lutfi.adidatatest.registration.token.VerificationToken;
import com.lutfi.adidatatest.registration.token.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private  final RegistrationCompleteEventListener eventListener;
    private final  HttpServletRequest servletRequest;
    @PostMapping
    public ResponseEntity<String> registerUser (@RequestParam("Name") String name,
                                                @RequestParam("Email") String email,
                                                @RequestParam("Password") String password,
                                                @RequestParam("Handphone") String noHandphone,
                                                @RequestParam("No_Kartu") String noKartu,
                                                @RequestParam("CVV") String cvv,
                                                @RequestParam("Expired_Kartu") String expiredKartu,
                                                @RequestParam("Pemegang_Kartu") String pemegangKartu){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setNo_Handphone(noHandphone);
        user.setNo_Kartu(Integer.valueOf(noKartu));
        user.setCVV(Integer.valueOf(cvv));
        user.setExpired_Kartu((expiredKartu));
        user.setPemegang_Kartu(pemegangKartu);



        try {
            User registeredUser = userService.registerUser(user);

            String applicationUrl = getApplicationUrl();

            // publish registration event
            publisher.publishEvent(new RegistrationCompleteEvent(registeredUser, applicationUrl));
            return ResponseEntity.ok("Succses! Please, check your email for to complete your registration");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }

    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        String url = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;

        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isIsEnable()){
            return "This account has Already been ferified, Please Login";

        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email virified successfully. Mow you can Login to yout Account";
        }
        return "Your virification token is expired, <a href =\""+url+"\"> Resend Verification Link.</a>";
    }

    @GetMapping("/resend-verification-token")

     public  String resendVerifiationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        resendVerifiationTokenEmail(theUser, applicationUrl(request),verificationToken);
        return "A new verification link Has Been Sent to your email";
    }

    private void resendVerifiationTokenEmail(User theUser, String applicationUrl,
                                             VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/verifyEmail?token="+verificationToken.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration : {}", url);

    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userService.findByEmail(email);
        String passwordResetUrl = "";
        if (user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), getApplicationUrl(), passwordResetToken, email);

        }
        return "Permintaan Terkirim Chek Email Anda, <a href='/login'>Kembali</a>";
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken, String email) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/ui-reset-password?token="+passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail(url, email);
        log.info("Click the link to Reset your password : {}", url);
        return  url;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("new-password")  String newPassword, @RequestParam("token") String passwordResetToken){
    String tokeValidationResult = userService.valdatePasswordResetToken(passwordResetToken);
    if (!tokeValidationResult.equalsIgnoreCase("valid")){
    return "Invalid password reset token";
    }
    User user = userService.findUserByPasswordToken(passwordResetToken);
    if (user != null){
        userService.resetUserPassword(user, newPassword);
        return "Password has been reset succesfully";

    }
    return "Invalid password reset token";

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    private String getApplicationUrl() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
