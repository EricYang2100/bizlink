import request from '@/utils/request'


// 查询详细
export function getDocAttribute(path) {
  return request({
    url: '/bizCell/docAttribute/' + path,
    method: 'get'
  })
}



// 修改
export function updateDocAttribute(data) {
  return request({
    url: '/bizCell/docAttribute',
    method: 'put',
    data: data
  })
}

