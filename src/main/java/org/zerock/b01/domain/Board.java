package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "imageSet") //Board에서도 BoardImage에 대한 참조를 가지는 방식으로 구조 변경
public class Board extends BaseEntity{
    /*
        JPA를 이용하는 개발의 핵심은 [객체지향을 통해서 영속 계층을 처리]하는데 있다.
        따라서 데이터에 해당하는 객체를 엔티티 객체라는 것으로 다루고, JPA로 이를 데이터베이스와 연동해서 관리한다.

        엔티티 객체는 PK(기본키)를 가지는 자바의 객체이다.
        엔티티 객체는 고유의 식별을 위해 @Id를 이용하여 객체를 구분하고 관리한다.

        Spring Data JPA는 자동으로 객체를 생성한다.
        이를 통해서 예외 처리 등을 자동으로 처리하는데, 이를 위해 제공되는 인터페이스가 JpaRepository이다.
    */
    /*
        개발의 첫 단계는 엔티티 객체를 생성하기 위한 [엔티티 클래스를 정의]하는 것이다.
        엔티티 클래스는 반드시 @Entity가 존재하고, 해당 엔티티 객체의 구분을 위한 @Id가 필요하다.

        게시물은 데이터베이스에 추가될 때 생성되는 번호(auto increment)를 이용할 것이므로
            [키 생성 전략(Key Generate Strategy)] 중에 GenerationType.IDENTITY로 DB에서 알아서 결정하는 방식을 이용한다.
                - IDENTITY : 데이터베이스에 위임 (MYSQL/MariaDB) - auto_increment
                - SEQUENCE : 데이터베이스 시퀀스 오브젝트 사용 (ORACLE) - @SequenceGenerator 필요
                - TABLE : 키 생성용 테이블 사용, 모든 DB에서 사용 - @TableGenerator 필요
                - AUTO : 방언에 따라 자동 지정, 기본값
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;


    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }


    /*
        @OneToMany는 기본적으로 각 엔티티에 해당하는 테이블을 독립적으로 생성하고, 중간에 매핑해주는 테이블이 생성된다.
            - board_image, board_image_set 둘 다 생성됨

        위와 같이 엔티티 테이블 사이에 생성되는 테이블을 '매핑 테이블'이라고 하는데,
            매핑 테이블을 생성하지 않는 방법은 다음과 같다.
            ① 단방향으로 @OneToMany를 이용하는 경우 @JoinColumn을 이용한다.
            ② mappedBy 속성을 이용한다.
                * Board와 BoardImage가 서로 참조를 유지하는 양방향 참조 상황에서 사용하는데
                    mappedBy는 '어떤 엔티티의 속성으로 매핑되는지'를 의미한다.

        상위 엔티티(Board)와 하위 엔티티(BoardImage)의 연관 관계를 상위 엔티티에서 관리하는 경우 신경써야 하는
            가장 중요한 점 중에 하나는 [상위 엔티티 객체의 상태가 변경되었을 때 하위 객체들도 같이 영향을 받는다는 점]이다.
            [JPA에서는 '영속성 전이(cascade)'라고 한다.]
            cascade의 속성값은 다음과 같다.
                - PERSIST / REMOVE : 상위 엔티티가 영속 처리될 때 하위 엔티티들도 같이 영속 처리
                - MERGE / REFRESH / DETACH : 상위 엔티티의 상태가 변경될 때 하위 엔티티들도 같이 상태 변경
                - ALL : 상위 엔티티의 모든 상태 변경이 하위 엔티티에 적용

        ※ orphanRemoval = true : 하위 엔티티의 참조가 더 이상 없는 상태가 되면 실제로 삭제함
     */
    //Board에서도 BoardImage에 대한 참조를 가지는 방식으로 구조 변경
    @OneToMany(mappedBy = "board",  //BoardImage의 board변수
                cascade = {CascadeType.ALL},
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();
    /*  ● @BatchSize
            @BatchSize에는 size라는 속성이 있는데, 이를 이용해서 'N번'에 해당하는 쿼리를 모아서 한번에 실행한다. (in 조건)
                여기서는 size 속성값만큼 BoardImage를 조회할 때 한번에 in 조건으로 사용된다.
                * in 조건은 조건의 범위를 지정하는데 사용되며, 지정된 값 중에서 하나 이상과 일치하면 조건에 맞는 것으로 처리한다.
    */


    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }


    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }
}
