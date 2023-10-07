package spring.data.entity;

import spring.common.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProductDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // 일대일
    // 하나의 상품에는 하나의 상품정보만 매핑되는 일대일 관계
    // OneToOne은 기본적으로 FetchType.EAGER임
    // FetchType.EAGER(즉시로딩)은 관련된 엔티티도 항상 함께 조회하게 됨
    @OneToOne
    @JoinColumn(name = "product_number")
    private Product product;

    public void updateProductDetail(String description, Product product) {
        this.description = description;
        this.product = product;
    }
}
