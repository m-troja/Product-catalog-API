package com.mtroja.productcatalog.dto.response;

import java.util.Map;

public record ProductResponse(
        Long id,
        String name,
        Long producerId,
        String producerName,
        Map<String, Object> attributes
) {}