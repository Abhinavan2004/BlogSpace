package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class Entity_User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name ;
    @Column(nullable = false)
    private LocalDateTime createdAt ;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity_User that = (Entity_User) o;
        return id == that.id && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, createdAt);
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
