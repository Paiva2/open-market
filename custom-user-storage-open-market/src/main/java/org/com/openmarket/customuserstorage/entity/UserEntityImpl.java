package org.com.openmarket.customuserstorage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_users")
public class UserEntityImpl {
    @Id
    @Column(name = "usr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "usr_username", nullable = false)
    private String userName;

    @Column(name = "usr_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usr_password", nullable = false)
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
