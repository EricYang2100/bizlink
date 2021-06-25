package com.welinkdata.gateway.goods.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "bizdoc_product_image")

/**
 * 产品图片
 * 产品:产品图片--1：n
 */
public class ProductImage extends MongoEntity {

    @Id
    private String id;

    /*  产品id */
    private String productId;

    /* 物理路径 */
    private String imgPath;


}
