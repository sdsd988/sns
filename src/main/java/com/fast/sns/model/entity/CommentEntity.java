package com.fast.sns.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Setter
@Getter
@Entity
@Table(name = "\"comment\"",indexes = {
        @Index(name = "post_id_idx",columnList = "post_id" )
})
@SQLDelete(sql = "UPDATE \"comment\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "comment")
    private String comment;


    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static CommentEntity of(UserEntity user, PostEntity postEntity,String comment) {
        CommentEntity entity = new CommentEntity();
        entity.setUser(user);
        entity.setPost(postEntity);
        entity.setComment(comment);

        return entity;
    }
}
