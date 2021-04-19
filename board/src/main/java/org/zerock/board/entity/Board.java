package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
//exclude = ~를 제외한
//writer로 Member테이블을 조인하면서 에러가 발생한다. 또는 불필요한 쿼리를 날린다.
@ToString(exclude = "writer")
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member writer;

}
