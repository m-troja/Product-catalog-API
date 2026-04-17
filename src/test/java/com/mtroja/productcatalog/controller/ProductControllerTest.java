package com.mtroja.productcatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtroja.productcatalog.dto.request.ProductRequest;
import com.mtroja.productcatalog.dto.response.ProductResponse;
import com.mtroja.productcatalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse response() {
        return new ProductResponse(1L, "iPhone", 1L, "Apple", Map.of());
    }

    @Test
    void shouldGetAll() throws Exception {
        when(service.findAll(any()))
                .thenReturn(new PageImpl<>(java.util.List.of(response())));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone"));
    }

    @Test
    void shouldGetOne() throws Exception {
        when(service.findById(1L)).thenReturn(response());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void shouldCreate() throws Exception {
        ProductRequest req = new ProductRequest("iPhone", 1L, Map.of());

        when(service.create(any())).thenReturn(response());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void shouldUpdate() throws Exception {
        ProductRequest req = new ProductRequest("iPhone 15", 1L, Map.of());

        when(service.update(eq(1L), any())).thenReturn(response());

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void shouldSearchByName() throws Exception {
        when(service.findByName(eq("iphone"), any()))
                .thenReturn(new PageImpl<>(java.util.List.of(response())));

        mockMvc.perform(get("/api/v1/products")
                        .param("name", "iphone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone"));
    }
}