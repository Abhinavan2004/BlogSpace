package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="Tags_Table")
public class Entity_Tags {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity_Tags that = (Entity_Tags) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
}
