# 概述
  维护合作伙伴相关信息


# Domain
* TPType
   
    树状结构定义合作伙伴类型
    
    字段：
    + id
    + name
    + code
    + parentId
    + fullPath （获取列表，fullPath-名称对应列表）
  
* TPInfo
  
    合作伙伴
  
    字段：
    + ID
    + name
    + city
    + detailAddress      
    + status
    + lastInvite    上一次邀请时间  
    + dept          所属部门
    + mgr           我方负责人，可多个
    + typeFullPath   tp 类型
    + tpTag         tag 数组
    
* TpComm
    合作伙伴通讯设置
    ID
    tpId
    commType  对接类型 ，目前支持门户和直连，未来扩充
  

* TPCommDirect
    合作伙伴直连对接
    
    + token
    + url
  
* TPUser
    合作伙伴门户访问用户定义  
    
  
* TPInvite
    
    给合作伙伴发出的邀请（用于合作伙伴激活）
  
    字段
    + ID
    + tpID
    + expireDate    截止期
    + url   （基于ID 创建?)
    + status 
    
* TPApply

    合作伙伴申请的连接

    字段
    + ID
    + TPURL
    + tpName
    + tpAddr
    + tpContact
    + tpContactPhone  
    + status

* TPMyDocShare

  与合作伙伴共享文档

  字段
    + ID
    + tpId
    + docConfigId
    + lastAccessTime
    
       
* TPDocShare
    合作伙伴共享的文档
  
    字段
        + ID
        + tpId
        + docConfigId
        + lastAccessTime
  
