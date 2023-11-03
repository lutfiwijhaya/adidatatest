package com.lutfi.adidatatest.registration.password;

import com.lutfi.adidatatest.User.User;
import com.lutfi.adidatatest.registration.token.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final  PasswordResetTokenRepository passwordResetTokenRepository;
    public void createPasswordResetTokenForUser(User user, String passwordToken){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);

    }

    public String validatePasswordResetToken(String theToken){

            PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
            if(token == null){
                return "Invalid Password Reset token";
            }
            User user = token.getUser();
            Calendar calendar = Calendar.getInstance();
            if(token.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){

                return "Token already expired";

            }
            return "Valid";
        }

        public Optional<User> findByPasswordToken(String passwordToken){
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
        }


}
