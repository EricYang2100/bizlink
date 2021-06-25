import request from '@/utils/request'

// 查询商品列表
export function listMyBankAccount(query) {
  return request({
    url: '/baseInfo/myBankAccount/list',
    method: 'get',
    params: query
  })
}

// 查询商品详细
export function getMyBankAccount(spuId) {
  return request({
    url: '/baseInfo/myBankAccount/' + spuId,
    method: 'get'
  })
}

// 新增商品
export function addMyBankAccount(data) {
  return request({
    url: '/baseInfo/myBankAccount',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateMyBankAccount(data) {
  return request({
    url: '/baseInfo/myBankAccount',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delMyBankAccount(spuId) {
  return request({
    url: '/baseInfo/myBankAccount/' + spuId,
    method: 'delete'
  })
}
