package com.neondomain.technicalexam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    public static final String lblNotNull = "The data cannot be null. Please verify the entered information";
    public static final String lblSize = "The data cannot be empty. Please verify the entered information";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull(message = lblNotNull)
    @Size(min = 1, message = lblSize)
    @Column(name = "user_name", unique = true)
    private String userName;

    @NotNull(message = lblNotNull)
    @Size(min = 1, message = lblSize)
    @Column(name = "password")
    private String password;

    @NotNull(message = lblNotNull)
    @Size(min = 1, message = lblSize)
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = lblNotNull)
    @Size(min = 1, message = lblSize)
    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private long age;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public long getAge() {
        return this.age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}
