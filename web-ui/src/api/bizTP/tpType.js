import request from '@/utils/request'

// 查询商品分类列表
export function listType(query) {
  return request({
    url: '/bizTP/tpType/list',
    method: 'get',
    params: query
  })
}


// 查询商品分类列表
export function dictList() {
  return request({
    url: '/bizTP/tpType/dictList',
    method: 'get'
    
  })
}

// 查询商品分类详细
export function getType(categoryId) {
  return request({
    url: '/bizTP/tpType/' + categoryId,
    method: 'get'
  })
}

// 新增商品分类
export function addType(data) {
  return request({
    url: '/bizTP/tpType',
    method: 'post',
    data: data
  })
}

// 修改商品分类
export function updateType(data) {
  return request({
    url: '/bizTP/tpType',
    method: 'put',
    data: data
  })
}

// 删除商品分类
export function delType(categoryId) {
  return request({
    url: '/bizTP/tpType/' + categoryId,
    method: 'delete'
  })
}

// 导出商品分类
export function exportType(query) {
  return request({
    url: '/bizTP/tpType/export',
    method: 'get',
    params: query
  })
}