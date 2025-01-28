package org.com.openmarket.users.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class UserEntity {
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

    @Column(name = "usr_enabled", nullable = false)
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "usr_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at", nullable = false)
    private Date updatedAt;
}
