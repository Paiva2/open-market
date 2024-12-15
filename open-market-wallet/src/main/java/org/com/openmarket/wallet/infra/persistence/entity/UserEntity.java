package org.com.openmarket.wallet.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usr_id")
    private UUID id;

    @Column(name = "usr_external_id")
    private String externalId;

    @Column(name = "usr_username")
    private String username;

    @Column(name = "usr_email")
    private String email;

    @CreationTimestamp
    @Column(name = "usr_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at")
    private Date updatedAt;
}
