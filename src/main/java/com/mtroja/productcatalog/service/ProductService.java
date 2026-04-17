package com.mtroja.productcatalog.service;

import com.mtroja.productcatalog.cnv.ProductCnv;
import com.mtroja.productcatalog.dto.request.ProductRequest;
import com.mtroja.productcatalog.dto.response.ProductResponse;
import com.mtroja.productcatalog.entity.Product;
import com.mtroja.productcatalog.entity.Producer;
import com.mtroja.productcatalog.exception.ProductNotFoundException;
import com.mtroja.productcatalog.exception.ProducerNotFoundException;
import com.mtroja.productcatalog.repository.ProductRepository;
import com.mtroja.productcatalog.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final ProductCnv productCnv;

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        log.debug("Fetching all products page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        return productRepository.findAll(pageable)
                .map(productCnv::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        log.debug("Fetching product id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found id={}", id);
                    return new ProductNotFoundException("Product ID " + id + " was not found");
                });

        return productCnv.toResponse(product);
    }

    public ProductResponse create(ProductRequest req) {
        log.info("Creating product name={}, producerId={}",
                req.name(), req.producerId());

        Producer producer = producerRepository.findById(req.producerId())
                .orElseThrow(() -> {
                    log.warn("Producer not found id={}", req.producerId());
                    return new ProducerNotFoundException(
                            "Producer " + req.producerId() + " was not found"
                    );
                });

        Product product = Product.builder()
                .name(req.name())
                .producer(producer)
                .attributes(req.attributes())
                .build();

        Product saved = productRepository.save(product);

        log.info("Product created id={}", saved.getId());

        return productCnv.toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest req) {
        log.info("Updating product id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found id={}", id);
                    return new ProductNotFoundException("Product " + id + " was not found");
                });

        Producer producer = producerRepository.findById(req.producerId())
                .orElseThrow(() -> {
                    log.warn("Producer not found id={}", req.producerId());
                    return new ProducerNotFoundException(
                            "Producer " + req.producerId() + " was not found"
                    );
                });

        product.setName(req.name());
        product.setProducer(producer);
        product.setAttributes(req.attributes());

        Product saved = productRepository.save(product);

        log.info("Product updated id={}", saved.getId());

        return productCnv.toResponse(saved);
    }

    public void delete(Long id) {
        log.info("Deleting product id={}", id);

        if (!productRepository.existsById(id)) {
            log.warn("Product not found for deletion id={}", id);
            throw new ProductNotFoundException("Product " + id + " was not found");
        }

        productRepository.deleteById(id);

        log.info("Product deleted id={}", id);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByProducer(Long producerId, Pageable pageable) {
        log.debug("Fetching products by producerId={}", producerId);

        return productRepository.findByProducerId(producerId, pageable)
                .map(productCnv::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByName(String name, Pageable pageable) {
        log.debug("Searching products by name={}", name);

        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(productCnv::toResponse);
    }
}