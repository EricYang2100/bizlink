import request from '@/utils/request'

// 查询列表
export function listDoc(query) {
  return request({
    url: '/bizDoc/docConfig/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getDocConfig(id) {
  return request({
    url: '/bizDoc/docConfig/' + id,
    method: 'get'
  })
}




// 新增
export function addDoc(data) {
  return request({
    url: '/bizDoc/docConfig',
    method: 'post',
    data: data
  })
}

// 修改
export function updateDoc(data) {
  return request({
    url: '/bizDoc/docConfig',
    method: 'put',
    data: data
  })
}

// 删除
export function delDoc(id) {
  return request({
    url: '/bizDoc/docConfig/' + id,
    method: 'delete'
  })
}

// 导出商品分类
export function exportDoc(query) {
  return request({
    url: '/bizDoc/docConfig/export',
    method: 'get',
    params: query
  })
}