package com.example.githubactions;

import java.util.List;

public class Recommendation {

    private List<String> role;
    private String location;
    private String date;

    public Recommendation(List<String> role, String location, String date) {
        this.role = role;
        this.location = location;
        this.date = date;
    }

    public Recommendation(){}

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "role=" + role +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
