package com.neondomain.technicalexam.model;

public class UserDTO {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private Long age;


    public UserDTO(Long id, String userName, String firstName, String lastName, Long age) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAge() {
        return this.age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}