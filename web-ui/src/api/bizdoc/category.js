import request from '@/utils/request'

// 查询商品分类列表
export function listCategory(query) {
  return request({
    url: '/goods/productCategory/list',
    method: 'get',
    params: query
  })
}

// 查询商品分类详细
export function getCategory(categoryId) {
  return request({
    url: '/goods/productCategory/' + categoryId,
    method: 'get'
  })
}

// 新增商品分类
export function addCategory(data) {
  return request({
    url: '/goods/productCategory',
    method: 'post',
    data: data
  })
}

// 修改商品分类
export function updateCategory(data) {
  return request({
    url: '/goods/productCategory',
    method: 'put',
    data: data
  })
}

// 删除商品分类
export function delCategory(categoryId) {
  return request({
    url: '/goods/productCategory/' + categoryId,
    method: 'delete'
  })
}

// 导出商品分类
export function exportCategory(query) {
  return request({
    url: '/goods/productCategory/export',
    method: 'get',
    params: query
  })
}