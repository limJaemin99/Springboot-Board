package org.zerock.b01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {
    /*
        게시물과 댓글, 게시물과 첨부파일의 관계를 테이블의 구조로 본다면 완전히 같은 구조이다.
            하지만 이를 JPA에서는 게시글을 중심으로 해석하는지, 첨부파일을 중심으로 해석하는지에 따라 다른 결과가 나올 수 있다.

        @OneToMany는 기본적으로 상위 엔티티(게시물)와 여러개의 하위 엔티티들(첨부파일)의 구조로 이루어진다.
            @ManyToOne과 다른 점은 @ManyToOne은 다른 엔티티 객체의 참조로 [FK를 가지는 쪽에서] 하는 방식이고,
            @OneToMany는 [PK를 가진 쪽에서] 사용한다는 점이다.

        @OneToMany를 사용하는 구조는 다음과 같은 특징을 가진다.
            - 상위 엔티티에서 하위 엔티티들을 관리한다.
            - JPA의 Repository를 상위 엔티티 기준으로 생성한다.
                하위 엔티티에 대한 Repository의 생성이 잘못된 것은 아니지만, 하위 엔티티들의 변경은 상위에도 반영되어야 한다.
            - 상위 엔티티 상태가 변경되면 하위 엔티티들의 상태들도 같이 처리해야 한다.
            - 상위 엔티티 하나와 하위 엔티티 여러개를 처리하는 경우 'N+1' 문제가 발생할 수 있으므로 주의해야 한다.
     */

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Board board;


    //BoardImage에는 Comparable 인터페이스를 적용하는데, 이는 @OneToMany 처리에서 순번에 맞게 정렬하기 위함이다.
    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }


    //Board 엔티티 삭제 시에 BoardImage 객체의 참조도 변경하기 위해 사용
    public void changeBoard(Board board){
        this.board = board;
    }

}
