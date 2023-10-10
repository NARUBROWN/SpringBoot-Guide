package jpa.data.entity;

import jpa.data.dto.request.ProductDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "product") // 양방향 관계에서 mappedBy = "table_name"을 활용하여 주인 관계 설정, 외래키를 주인쪽에서 가질 수 있도록 해줌
    @ToString.Exclude  //  양방향 관계에서는 순환참조가 발생하기 때문에, Exclude를 사용하여 ToString을 제외시켜준다.
    private ProductDetail productDetail;

    public Product(ProductDTO productDTO){
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.stock = productDTO.getStock();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Product(Long number, String name, int price, int stock) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void updateProduct(String name, LocalDateTime updatedAt){
        this.name = name;
        this.updatedAt = updatedAt;
    }
}
