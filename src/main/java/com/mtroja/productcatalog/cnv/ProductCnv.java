package com.mtroja.productcatalog.cnv;

import com.mtroja.productcatalog.dto.response.ProductResponse;
import com.mtroja.productcatalog.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCnv {

    public ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getProducer().getId(),
                p.getProducer().getName(),
                p.getAttributes()
        );
    }
}