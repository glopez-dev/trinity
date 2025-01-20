package com.trinity.user.domain.model;

import com.trinity.user.domain.enums.UserStatus;
import com.trinity.user.domain.events.AccountDeleted;
import com.trinity.user.domain.events.UserConnected;
import com.trinity.user.domain.events.UserEvent;
import com.trinity.user.domain.valueobjects.PersonalInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public abstract class User {
    @Id
    @Column(name = "id")
    private final UUID id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Embedded
    PersonalInfo personalInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    protected UserStatus status;

    @Column(name = "created_at")
    protected final LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    protected LocalDateTime lastLoginAt;

    @Transient
    @Builder.Default
    protected List<UserEvent> domainEvents = new ArrayList<>();

    public void delete(String reason) {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("User is already deleted");
        }
        this.status = UserStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
        domainEvents.add(new AccountDeleted(id, reason, LocalDateTime.now(), null));
    }

    public void connect(String ipAddress, String userAgent) {
        this.lastLoginAt = LocalDateTime.now();
        domainEvents.add(new UserConnected(id, ipAddress, userAgent, LocalDateTime.now()));
    }

    protected void addDomainEvent(UserEvent event) {
        this.domainEvents.add(event);
    }
}
