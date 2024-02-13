package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Board {
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
}
