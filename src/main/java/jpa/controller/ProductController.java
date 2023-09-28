package jpa.controller;

import io.swagger.annotations.ApiImplicitParam;
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
    // 코드 변경 테스트
    private final ProductService productService;

    @ApiImplicitParam(name="X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 ACCESS_TOKEN", required = true, dataTypeClass = String.class, paramType = "header")
    @GetMapping()
    public ResponseEntity<ProductResponseDto> getProduct(Long number) {
        ProductResponseDto productResponseDto = productService.getProduct(number);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiImplicitParam(name="X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 ACCESS_TOKEN", required = true, dataTypeClass = String.class, paramType = "header")
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDTO productDTO) {
        ProductResponseDto productResponseDto = productService.saveProduct(productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiImplicitParam(name="X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 ACCESS_TOKEN", required = true, dataTypeClass = String.class, paramType = "header")
    @PutMapping()
    public ResponseEntity<ProductResponseDto> changeProductName(@RequestParam(name = "number") Long number,
                                                                @RequestParam(name ="name") String name) throws Exception {
        ProductResponseDto productResponseDto = productService.changeProductName(number, name);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiImplicitParam(name="X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 ACCESS_TOKEN", required = true, dataTypeClass = String.class, paramType = "header")
    @DeleteMapping()
    public ResponseEntity<String> deleteProduct(Long number) throws Exception {
        productService.deleteProduct(number);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }

}
