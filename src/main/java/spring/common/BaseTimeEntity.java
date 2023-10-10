package spring.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA의 엔티티 클래스가 상속받을 경우 자식 클래스에게 매핑정보를 전달
@EntityListeners(AuditingEntityListener.class) // 엔티티를 데이터베이스에 적용하기 전후로 콜백을 요청할 수 있게 하는 어노테이션입니다.
// AuditingEntityListener.class: 엔티티의 Auditing 정보를 주입하는 JPA엔티티 리스너 클래스입니다.
public class BaseTimeEntity {
    @CreatedDate // 데이터 생성 날짜를 자동으로 주입해주는 어노테이션입니다.
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터 수정 날짜를 자동으로 주입해주는 어노테이션입니다.
    private LocalDateTime updatedAt;

}
