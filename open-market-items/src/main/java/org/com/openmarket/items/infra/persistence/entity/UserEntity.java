package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usr_id")
    private UUID id;

    @Column(name = "usr_external_id", unique = true, nullable = false)
    private String externalId;

    @Column(name = "usr_username", nullable = false)
    private String userName;

    @Column(name = "usr_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usr_enabled", nullable = false)
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "usr_created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ItemAlterationEntity> itemAlterations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserItemEntity> userItems;
}
