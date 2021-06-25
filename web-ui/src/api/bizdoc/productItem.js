import request from '@/utils/request'

// 查询列表
export function listItem(query) {
  return request({
    url: '/goods/productItem/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getItem(id) {
  return request({
    url: '/goods/productItem/' + id,
    method: 'get'
  })
}




// 新增
export function addItem(data) {
  return request({
    url: '/goods/productItem',
    method: 'post',
    data: data
  })
}

// 修改
export function updateItem(data) {
  return request({
    url: '/goods/productItem',
    method: 'put',
    data: data
  })
}

// 删除
export function delItem(id) {
  return request({
    url: '/goods/productItem/' + id,
    method: 'delete'
  })
}

// 导出商品分类
export function exportItem(query) {
  return request({
    url: '/goods/productItem/export',
    method: 'get',
    params: query
  })
}