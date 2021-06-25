package com.welinkdata.gateway.goods.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.HashMap;


@Data
@ToString
@Document(collection = "bizdoc_product_item")
/**
 * 产品信息
 * --产品支持动态属性。
 */

public class ProductItem extends MongoEntity {

    @Id
    private String id;

    /*  分类 */
//    @Field("category_id")
//    @DBRef
//    private ProductCategory category;

    /*  分类Id */
    private String categoryId;

    /*  产品名称 */
    private String  name;

    /*  产品编码 */
    private String  code;

    /*  产品规格 */
    private String  spec;

    /*  产品单位 */
    private String  uom;

    /*  产品基准价格 */
    private BigDecimal basePrice;


    /** 分类状态（0正常 1停用） */
    private String status;


    /*  动态字段 */
    private HashMap df = new HashMap();

}
