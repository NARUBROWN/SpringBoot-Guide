package jpa.data.dto.response;

import jpa.data.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private  Long number;
    private String name;
    private int price;
    private int stock;

    public ProductResponseDto(Product product) {
        this.number = product.getNumber();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }

    public ProductResponseDto(long number, String name, int price, int stock) {
        this.name = name;
        this.number = number;
        this.price = price;
        this.stock = stock;
    }
}
