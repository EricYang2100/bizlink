import request from '@/utils/request'

// 查询列表
export function listTreeItem(query) {
  return request({
    url: '/bizCell/docTreeItem/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getTreeItem(categoryId) {
  return request({
    url: '/bizCell/docTreeItem/' + categoryId,
    method: 'get'
  })
}


// 查询详细
export function getTreeItemByPath(path) {
  return request({
    url: '/bizCell/docTreeItem/pathSelect/' + path,
    method: 'get'
  })
}

// 新增
export function addTreeItem(data) {
  return request({
    url: '/bizCell/docTreeItem',
    method: 'post',
    data: data
  })
}

// 修改
export function updateTreeItem(data) {
  return request({
    url: '/bizCell/docTreeItem',
    method: 'put',
    data: data
  })
}

// 删除
export function delTreeItem(categoryId) {
  return request({
    url: '/bizCell/docTreeItem/' + categoryId,
    method: 'delete'
  })
}

// 导出
export function exportTreeItem(query) {
  return request({
    url: '/bizCell/docTreeItem/export',
    method: 'get',
    params: query
  })
}