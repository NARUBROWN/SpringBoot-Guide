package spring.data.entity;

import spring.common.BaseTimeEntity;
import spring.data.dto.request.ProductDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @OneToOne(mappedBy = "product") // mapperdBy는 양방향 관계에서 어떤 엔티티가 주인인지 나타내는 옵션이다.
    @ToString.Exclude // 양방향 관계때 ToString을 사용하면 순환참조가 발생한다. 때문에 Exclude를 사용하여 ToString을 제외시킨다.
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    @ToString.Exclude
    private Provider provider;

    public Product(ProductDTO productDTO){
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.stock = productDTO.getStock();
    }

    @Builder
    public Product(Long number, String name, int price, int stock) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void updateProduct(String name){
        this.name = name;
    }
}
