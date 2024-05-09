package org.ying.book.dto.user;

import java.util.ArrayList;

public class UserInfoDto {
    private String email;

    private ArrayList<String> roles;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }
}
