import request from '@/utils/request'

// 查询列表
export function listDoc(query) {
  return request({
    url: '/dp/myDyncDoc/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getDoc(id) {
  return request({
    url: '/dp/myDyncDoc/' + id,
    method: 'get'
  })
}




// 新增
export function addDoc(data) {
  return request({
    url: '/dp/myDyncDoc',
    method: 'post',
    data: data
  })
}

// 修改
export function updateDoc(data) {
  return request({
    url: '/dp/myDyncDoc',
    method: 'put',
    data: data
  })
}

// 删除
export function delDoc(id) {
  return request({
    url: '/dp/myDyncDoc/' + id,
    method: 'delete'
  })
}

// 导出商品分类
export function exportDoc(query) {
  return request({
    url: '/dp/myDyncDoc/export',
    method: 'get',
    params: query
  })
}