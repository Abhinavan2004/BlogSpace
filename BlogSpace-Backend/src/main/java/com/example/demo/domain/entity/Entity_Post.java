package com.example.demo.domain.entity;

import com.example.demo.domain.Enum_Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@Table(name="Posts_Table")
public class Entity_Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id ;

    @Column(nullable = false)
    private String title ;

    @Column(nullable = false, columnDefinition="TEXT")
    private String content ;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Enum_Post status ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Entity_User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", nullable = false)
    private Entity_Category category ;

    @Column(nullable = false)
    private Integer readingTime;

    @Column(nullable = false)
    private LocalDateTime createdAt ;

    @Column(nullable = false)
    private LocalDateTime updatedAt ;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity_Post that = (Entity_Post) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && status == that.status && Objects.equals(readingTime, that.readingTime) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, readingTime, createdAt, updatedAt);
    }

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }



}
