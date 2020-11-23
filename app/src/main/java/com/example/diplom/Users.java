package com.example.diplom;

public class Users {
    public String Fio, Email,  Role;
    public Integer Points;

    public Users(){
    }

    public Users(String fio, String email, String role, Integer points) {
        Fio = fio;
        Email = email;
        Role = role;
        Points = points;
    }
}
