package com.socialpetwork.domain;

public class UserDTO {
    private String password;
    private Long id;
    private String name;
    private String birthday;
    private String email;
    private String username;

    // Constructor
    public UserDTO(long l, String johnDoe, String mail){
    }

    // Parameterized Constructor
    public UserDTO(Long id, String name, String birthday, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public UserDTO() {}

    public UserDTO(long l, String user) {
        this.id = l;
        this.name = user;
    }

    // Getters and Setters

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

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
