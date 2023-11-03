package com.lutfi.adidatatest.User;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private  String Name;
    @NaturalId(mutable = true)
    private  String email;
    private String  Password;
    private  String No_Handphone;
    private Integer No_Kartu;
    private Integer CVV;
    private  String Expired_Kartu;
    private String Pemegang_Kartu;
    private  boolean IsEnable = false;



}
