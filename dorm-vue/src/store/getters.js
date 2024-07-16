const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.token,
  avatar: state => state.user.avatar,
  loginUser: state => state.currUser
}
export default getters
