package com.welinkdata.gateway.goods.repository;

import com.welinkdata.gateway.goods.domain.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

    ProductCategory findFirstByFullPath(String fullPath);
}
