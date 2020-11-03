package com.lafarleaf.leafolio.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private long id;
  
  @Column(name="fname", columnDefinition="VARCHAR(255) NOT NULL")
  private String fName;
  
  @Column(name="lname", columnDefinition="VARCHAR(255) NOT NULL")
  private String lName;
  
  private String email;

  @Column(name="password", columnDefinition="VARCHAR(255) NOT NULL")
  private String password;
  
  public User(String fName, String lName, String email, String password) {
    this.fName = fName;
    this.lName = lName;
    this.email = email;
    this.password = password;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getfName() {
    return fName;
  }
  public void setfName(String fName) {
    this.fName = fName;
  }
  public String getlName() {
    return lName;
  }
  public void setlName(String lName) {
    this.lName = lName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
}
