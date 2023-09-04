package jpa.data.entity;

import jpa.data.dto.request.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Product(ProductDTO productDTO){
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.stock = productDTO.getStock();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProduct(String name, LocalDateTime updatedAt){
        this.name = name;
        this.updatedAt = updatedAt;
    }
}
