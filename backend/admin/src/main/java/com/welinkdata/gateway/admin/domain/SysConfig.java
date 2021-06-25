package com.welinkdata.gateway.admin.domain;

import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@ToString
@Document(collection = "sys_config")
public class SysConfig extends MongoEntity {

    /** 参数主键 */
    @Id
    private String configId;

    /** 参数名称 */

    private String configName;

    /** 参数键名 */

    private String configKey;

    /** 参数键值 */

    private String configValue;

    /** 系统内置（Y是 N否） */

    private String configType;

}
