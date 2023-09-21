package jpa.service;

import jpa.data.dto.request.ProductDTO;
import jpa.data.dto.response.ProductResponseDto;
import jpa.data.entity.Product;
import jpa.data.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

// 배포가 잘 됐으면 좋겠 마지막
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto getProduct(Long number) {
        return new ProductResponseDto(productRepository.findById(number).orElseThrow(RuntimeException::new));
    }

    public ProductResponseDto saveProduct(ProductDTO productDto) {
        Product product = new Product(productDto);
        return new ProductResponseDto(productRepository.save(product));
    }

    public ProductResponseDto changeProductName(Long number, String name) throws Exception {
        Optional<Product> selectedProduct = productRepository.findById(number);

        Product updatedProduct;
        if (selectedProduct.isPresent()) {
            Product product = selectedProduct.get();
            product.updateProduct(name, LocalDateTime.now());
            updatedProduct = productRepository.save(product);
        } else {
            throw new Exception();
        }

        return new ProductResponseDto(updatedProduct);
    }

    public void deleteProduct(Long number) throws Exception {
        Optional<Product> selectedProduct = productRepository.findById(number);

        if (selectedProduct.isPresent()) {
            Product product = selectedProduct.get();
            productRepository.delete(product);
        } else {
            throw new Exception();
        }
    }
}
