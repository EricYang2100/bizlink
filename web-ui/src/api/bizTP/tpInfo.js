import request from '@/utils/request'

// 查询参数列表
export function listInfo(query) {
  return request({
    url: '/bizTP/tpInfo/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getInfo(configId) {
  return request({
    url: '/bizTP/tpInfo/' + configId,
    method: 'get'
  })
}


// 新增参数配置
export function addInfo(data) {
  return request({
    url: '/bizTP/tpInfo',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateInfo(data) {
  return request({
    url: '/bizTP/tpInfo',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delInfo(configId) {
  return request({
    url: '/bizTP/tpInfo/' + configId,
    method: 'delete'
  })
}


// 导出参数
export function exportInfo(query) {
  return request({
    url: '/bizTP/tpInfo/export',
    method: 'get',
    params: query
  })
}