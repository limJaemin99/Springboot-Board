package org.zerock.b01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {
    /*
        데이터베이스의 대부분의 테이블에는 데이터가 추가/수정된 시간 등이 컬럼으로 작성된다.
        자바에서는 @MappedSuperClass를 이용하여 공통으로 사용되는 컬럼들을 지정하고, 해당 클래스를 상속해서 이를 쉽게 처리한다.

        여기서 가장 중요한 부분은 Spring Data JPA의 AuditingEntityListener를 지정하는 부분이다.
        AuditingEntityListener를 적용하면 엔티티가 DB에 추가/변경될 때 자동으로 시간값을 지정할 수 있다.
        AuditingEntityListener를 활성화시키려면 프로젝트 설정에 @EnableJpaAuditing을 추가해줘야 한다. (B01Application 파일)
    */
    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
