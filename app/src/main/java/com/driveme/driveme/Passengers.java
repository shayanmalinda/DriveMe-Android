package com.driveme.driveme;

public class Passengers {
public Passengers(String name,String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
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


    private String email;
    private String name;
    private String address;
    private String phone;
}
