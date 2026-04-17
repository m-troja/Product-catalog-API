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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private ProductCnv productCnv;

    @InjectMocks
    private ProductService service;

    private ProductResponse response() {
        return new ProductResponse(1L, "iPhone", 1L, "Apple", Map.of());
    }

    private Producer producer() {
        return Producer.builder()
                .id(1L)
                .name("Apple")
                .build();
    }

    private Product product() {
        return Product.builder()
                .id(1L)
                .name("iPhone")
                .producer(producer())
                .attributes(Map.of("color", "black"))
                .build();
    }

    @Test
    void shouldFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product()));

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<?> result = service.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findAll(pageable);
    }

    @Test
    void shouldFindById() {
        Product product = product();
        ProductResponse response = response();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productCnv.toResponse(product)).thenReturn(response); // ✅ FIX

        var result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("iPhone");
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest req = new ProductRequest("iPhone", 1L, Map.of());

        Producer producer = producer();
        Product product = product();

        when(producerRepository.findById(1L)).thenReturn(Optional.of(producer));
        when(productRepository.save(any())).thenReturn(product);
        when(productCnv.toResponse(product)).thenReturn(response());

        var result = service.create(req);

        assertThat(result.name()).isEqualTo("iPhone");
        verify(productRepository).save(any(Product.class));
        verify(productCnv).toResponse(product);
    }

    @Test
    void shouldThrowWhenProducerNotFoundOnCreate() {
        ProductRequest req = new ProductRequest("iPhone", 1L, Map.of());

        when(producerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ProducerNotFoundException.class);
    }

    @Test
    void shouldUpdateProduct() {
        Product existing = product();
        Producer producer = producer();
        ProductRequest req = new ProductRequest("iPhone 15", 1L, Map.of());

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(producerRepository.findById(1L)).thenReturn(Optional.of(producer));
        when(productRepository.save(existing)).thenReturn(existing);
        when(productCnv.toResponse(existing)).thenReturn(response());

        var result = service.update(1L, req);

        assertThat(result.name()).isEqualTo("iPhone");
        verify(productRepository).save(existing); // ✅ important
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExisting() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldFindByProducer() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByProducerId(eq(1L), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(product())));

        var result = service.findByProducer(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldFindByName() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByNameContainingIgnoreCase(eq("iphone"), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(product())));

        var result = service.findByName("iphone", pageable);

        assertThat(result.getContent()).hasSize(1);
    }
}