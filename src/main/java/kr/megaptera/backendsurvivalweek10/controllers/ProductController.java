package kr.megaptera.backendsurvivalweek10.controllers;

import kr.megaptera.backendsurvivalweek10.application.product.CreateProductService;
import kr.megaptera.backendsurvivalweek10.application.product.GetProductListService;
import kr.megaptera.backendsurvivalweek10.dtos.CreateProductDto;
import kr.megaptera.backendsurvivalweek10.dtos.ProductListDto;
import kr.megaptera.backendsurvivalweek10.models.Money;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {
    private final GetProductListService getProductListService;
    private final CreateProductService createProductService;

    public ProductController(GetProductListService getProductListService,
                             CreateProductService createProductService) {
        this.getProductListService = getProductListService;
        this.createProductService = createProductService;
    }

    @GetMapping
    public ProductListDto list() {
        return getProductListService.getProductListDto();
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateProductDto dto) {
        String name = dto.name().strip();
        Money price = new Money(dto.price());

        createProductService.createProduct(name, price);
    }
}
