package jpa.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // 일대일
    // 하나의 상품에는 하나의 상품정보만 매핑되는 일대일 관계
    @OneToOne
    @JoinColumn(name = "product_number")
    private Product product;

    public ProductDetail(Product product, String description) {
        this.product = product;
        this.description = description;
    }
}
