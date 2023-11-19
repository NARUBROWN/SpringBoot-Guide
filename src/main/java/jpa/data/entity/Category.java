package jpa.data.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;

    @OneToMany(fetch = FetchType.EAGER) // 일대다 연관관계에서는 연관관계 설정을 위해 update 쿼리가 발생한다. 이런 문제를
    // 해결하기 위해서는 일대다 양방향 관계보다는 다대일 연관관계를 사용하는 것이 좋다.
    @JoinColumn(name = "category_id")
    private List<Product> products = new ArrayList<>();

    @Builder
    public Category (Long id, String code, String name, Product product) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.products.add(product);
    }
}
