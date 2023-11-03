package com.lutfi.adidatatest.registration;

import org.hibernate.annotations.NaturalId;

public record RegistrationRequest(  String Name,
        String Email,
        String  Password,
        Integer No_Hanphone,
        Integer No_Kartu,
        Integer CVV,
        Integer Expired_Kartu,
        String Pemegang_Kartu,
        String role) {
}
