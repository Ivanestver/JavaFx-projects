package com.example.wordscloud.models;

public class User {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != User.class)
            return false;
        if (this == obj)
            return true;

        User u = (User) obj;
        return this.login.equals(u.login) && this.password.equals(u.password);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + login.hashCode();
        hash = 31 * hash + password.hashCode();
        return hash;
    }
}
