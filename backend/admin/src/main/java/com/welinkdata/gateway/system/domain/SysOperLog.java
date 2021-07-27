package com.welinkdata.gateway.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welinkdata.gateway.common.core.domain.MongoEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@ToString
@Document(collection = "sys_oper_log")
public class SysOperLog extends MongoEntity {


    /** 日志主键 */
    @Id
    private String operId;

    /** 操作模块 */

    private String title;

    /** 业务类型（0其它 1新增 2修改 3删除 4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据"） */
    private Integer businessType;

    /** 业务类型数组 */
    private Integer[] businessTypes;

    /** 请求方法 */
    private String method;

    /** 请求方式 */
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */

    private Integer operatorType;

    /** 操作人员 */

    private String operName;

    /** 部门名称 */

    private String deptName;

    /** 请求url */

    private String operUrl;

    /** 操作地址 */

    private String operIp;

    /** 操作地点 */

    private String operLocation;

    /** 请求参数 */

    private String operParam;

    /** 返回参数 */

    private String jsonResult;

    /** 操作状态（0正常 1异常） */

    private Integer status;

    /** 错误消息 */

    private String errorMsg;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Date operTime;
}
