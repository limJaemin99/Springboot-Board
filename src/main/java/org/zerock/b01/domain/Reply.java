package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Reply",
        indexes = {
            @Index(name = "idx_reply_board_bno",columnList = "board_bno")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString//(exclude = "board")    // 참조하는 객체를 사용하지 않도록 exclude 속성값을 지정
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)  // 연관 관계를 나타낼 때는 반드시 fetch 속성을 LAZY로 지정
    private Board board;

    private String replyText;

    private String replyer;


    //댓글 수정 메소드
    public void changeText(String text){
        this.replyText = text;
    }

}
