package com.socialpetwork.domain;

public class UserDTO {
    private Long id;
    private String name;
    private String birthday;
    private String email;
    private String username;
    private String profileUrl;

    // Constructor
    public UserDTO(){
    }

    // Parameterized Constructor
    public UserDTO(Long id, String name, String birthday, String email, String username, String profileUrl) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    // Getter and Seters

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }




}
