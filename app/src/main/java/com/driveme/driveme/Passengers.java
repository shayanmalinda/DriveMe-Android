package com.driveme.driveme;

public class Passengers {
    public Passengers(String email, String address, String phone, String password) {
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
    }


    public String getEmail() {
        return email;
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
    private String address;
    private String phone;
    private String password;
}
