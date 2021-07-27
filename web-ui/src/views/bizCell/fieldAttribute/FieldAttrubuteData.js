
export const fieldTreeData = [
  {
    title: '订单头',
    sysName: 'header',
    dataType: 'Record',
    remark: '',    
    htmlType: 'input',
    key: '0',
    isRequired: '1',
    children: [
      {
        title: '客户',
        sysName: 'customer',
        dataType: 'Record',
        remark: '',       
        htmlType: 'input',
        key: '1',
        isRequired: '1',
        children: [
          {
            title: '客户名称',
            sysName: 'customerName',
            dataType: 'String',
            remark: '',            
            htmlType: 'input',
            key: '2',
            isRequired: '1',
          },
          {
            title: '客户编码',
            sysName: 'customerCode',
            dataType: 'String',
            remark: '123',            
            editType: 'text',
            key: '3',
            isRequired: '1',

          },

          
        ]
      },
      
      {
        title: '订单编号',
        sysName: 'orderNo',
        dataType: 'String',
                
        htmlType: 'input',
        key: '4',
        isRequired: '1',
      },
      {
        title: '订单日期',
        sysName: 'orderDate',
        dataType: 'Date',
        remark: '',        
        
        htmlType: 'Date',
        key: '5',
        isRequired: '1',
      },
      
    ]
  },
  {
    title: '订单明细',
    sysName: 'orderItems',
    dataType: 'List',
    remark: '',
   
    key: '6',
    isRequired: '1',
    children: [
      {
        title: '商品编码',
        sysName: 'productCode',
        dataType: 'String',
        remark: '',   
        htmlType: 'input',
        key: '7',
        isRequired: '0'
      },
      {
        title: '商品名',
        sysName: 'productName',
        dataType: 'String',
        remark: '',   
        htmlType: 'input',
        key: '8',
        isRequired: '0'
      },
     
    ]
  }
]
