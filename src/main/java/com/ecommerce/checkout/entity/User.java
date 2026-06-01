package com.ecommerce.checkout.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    public User() {}
    public User(Long id, String name, String email) { this.id = id; this.name = name; this.email = email; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public static Builder builder() { return new Builder(); }
    public static class Builder { private Long id; private String name; private String email;
        public Builder id(Long id){this.id=id; return this;} public Builder name(String name){this.name=name; return this;} public Builder email(String email){this.email=email; return this;}
        public User build(){ return new User(id,name,email); }
    }
}
