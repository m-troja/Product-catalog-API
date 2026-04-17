package com.mtroja.productcatalog.repository;

import com.mtroja.productcatalog.entity.Producer;
import com.mtroja.productcatalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer,Long> {}