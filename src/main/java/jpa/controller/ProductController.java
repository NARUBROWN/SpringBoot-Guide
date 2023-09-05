package jpa.controller;

import jpa.data.dto.request.ProductDTO;
import jpa.data.dto.response.ProductResponseDto;
import jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<ProductResponseDto> getProduct(Long number) {
        ProductResponseDto productResponseDto = productService.getProduct(number);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDTO productDTO) {
        ProductResponseDto productResponseDto = productService.saveProduct(productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @PutMapping()
    public ResponseEntity<ProductResponseDto> changeProductName(@RequestParam(name = "number") Long number,
                                                                @RequestParam(name ="name") String name) throws Exception {
        ProductResponseDto productResponseDto = productService.changeProductName(number, name);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteProduct(Long number) throws Exception {
        productService.deleteProduct(number);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }

}
