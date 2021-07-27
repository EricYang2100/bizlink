package com.welinkdata.gateway.bizApp.saleGoods.repository;

import com.welinkdata.gateway.bizApp.saleGoods.domain.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

    ProductCategory findFirstByFullPath(String fullPath);
}
