package com.driveme.driveme;

public class Passengers {
    public Passengers(String name,String email, String address, String phone, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    private String email;
    private String name;
    private String address;
    private String phone;
    private String password;
}
