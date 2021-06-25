import request from '@/utils/request'

// 查询列表
export function listField(query) {
  return request({
    url: '/dp/docField/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getField(id) {
  return request({
    url: '/dp/docField/' + id,
    method: 'get'
  })
}

// 新增
export function addField(data) {
  return request({
    url: '/dp/docField',
    method: 'post',
    data: data
  })
}

// 修改
export function updateField(data) {
  return request({
    url: '/dp/docField',
    method: 'put',
    data: data
  })
}

// 删除
export function delField(id) {
  return request({
    url: '/dp/docField/' + id,
    method: 'delete'
  })
}

// 导出商品分类
export function exportField(query) {
  return request({
    url: '/dp/docField/export',
    method: 'get',
    params: query
  })
}