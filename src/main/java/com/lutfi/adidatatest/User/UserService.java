package com.lutfi.adidatatest.User;

import com.lutfi.adidatatest.exception.UserAlreadyExistsException;
import com.lutfi.adidatatest.registration.RegistrationRequest;
import com.lutfi.adidatatest.registration.password.PasswordResetTokenService;
import com.lutfi.adidatatest.registration.token.VerificationToken;
import com.lutfi.adidatatest.registration.token.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }




    @Override
    public User registerUser(User user) {
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Email : " + user.getEmail() + " Sudah Tersedia");
        }

        // Mengisi objek newUser dengan data dari objek user yang diterima
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setNo_Handphone(user.getNo_Handphone());
        newUser.setNo_Kartu(user.getNo_Kartu());
        newUser.setCVV(user.getCVV());
        newUser.setExpired_Kartu(user.getExpired_Kartu());
        newUser.setPemegang_Kartu(user.getPemegang_Kartu());


        return userRepository.save(newUser);
    }


    @Override
    public Optional<User> findByEmail(String Email) {
        return userRepository.findByEmail(Email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);

    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid Verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if(token.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0){

            return "Token alreadyexpired";

        }
        user.setIsEnable(true);
        userRepository.save(user);
        return "Valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        var tokenExpirationTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(tokenExpirationTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
    }

    @Override
    public String valdatePasswordResetToken(String passwordResetToken) {
        return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
    }

    @Override
    public User findUserByPasswordToken(String passwordResetToken) {
       return passwordResetTokenService.findByPasswordToken(passwordResetToken).get();

    }

    @Override
    public void resetUserPassword(User user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    }
}
