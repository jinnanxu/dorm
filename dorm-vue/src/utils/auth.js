import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  let token = localStorage.getItem('AuthToken')
  if(!token){
    return ''
  }
  // console.log('从Storage获取Token', token)
  return token
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
