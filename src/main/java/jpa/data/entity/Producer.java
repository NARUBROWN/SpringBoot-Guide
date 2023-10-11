package jpa.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "producer")
// 다대다 단방향 매핑 연습을 위한 클래스
public class Producer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    // 대다대는 사용하지 않는 방식이다.
    @ManyToMany
    @ToString.Exclude
    private List<Product> products = new ArrayList<>(); // 리스트를 필드로 가지는 객체에서는 외래키를 가지지 않기 때문에 @JoinColumn을 설정하지 않아도 됨

    public void addProduct(Product product) {
        this.products.add(product);
    }

    @Builder
    public Producer (Long id, String code, String name, Product product) {
        this.id = id;
        this.code = code;
        this.name = name;
        addProduct(product);
    }

}
