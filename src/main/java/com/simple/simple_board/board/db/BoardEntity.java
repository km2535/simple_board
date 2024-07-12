package com.simple.simple_board.board.db;

import com.simple.simple_board.post.db.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLOrder;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardName;

    private String status;

    //포스트와 1대 N 연결

    @OneToMany(
            mappedBy = "board"
    )
    @SQLRestriction("status='REGISTERED'")
    @SQLOrder(value = "id desc")
    private List<PostEntity> postList = List.of();
}
