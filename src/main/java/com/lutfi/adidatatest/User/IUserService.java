package com.lutfi.adidatatest.User;

import com.lutfi.adidatatest.registration.RegistrationRequest;
import com.lutfi.adidatatest.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();


    User registerUser(User user);

    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String theToken);

    VerificationToken generateNewVerificationToken(String oldToken);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    String valdatePasswordResetToken(String passwordResetToken);

    User findUserByPasswordToken(String passwordResetToken);

    void resetUserPassword(User user, String newPassword);
}
