package com.mtroja.productcatalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ProductRequest(
        @NotBlank String name,
        @NotNull Long producerId,
        Map<String, Object> attributes
) {}