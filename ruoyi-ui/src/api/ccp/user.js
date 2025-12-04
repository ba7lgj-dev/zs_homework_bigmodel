import request from '@/utils/request'

export function listUser(query) {
  return request({
    url: '/ccp/user/list',
    method: 'get',
    params: query
  })
}

export function getUser(id) {
  return request({
    url: `/ccp/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/ccp/user',
    method: 'post',
    data: data
  })
}

export function updateUser(data) {
  return request({
    url: '/ccp/user',
    method: 'put',
    data: data
  })
}

export function delUser(id) {
  return request({
    url: `/ccp/user/${id}`,
    method: 'delete'
  })
}

export function delUserBatch(ids) {
  return request({
    url: `/ccp/user/batch/${ids}`,
    method: 'delete'
  })
}

export function changeUserStatus(data) {
  return request({
    url: '/ccp/user/changeStatus',
    method: 'put',
    data: data
  })
}

export function reviewUser(data) {
  return request({
    url: '/ccp/user/review',
    method: 'put',
    data: data
  })
}

export function exportUser(query) {
  return request({
    url: '/ccp/user/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
