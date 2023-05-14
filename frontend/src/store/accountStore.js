import {  requestLogin, getUserInfo, requestConfirm, requestSignin } from "../../common/api/commonAPI";
import router from '../router';

const state = {
  accessToken: null,
  refreshToken: null,
  isLogin: false,
  signRegister: false,
  userId: null,
  isGuide: null,
  myName: null,
};

const getters = {
  getToken: state => {
    return state.token;
  },
  getLogin: state => {
    return state.isLogin
  },
  getSignRegister: state => {
    return state.signRegister
  },
  getIsLogin: state => {
    return state.isLogin
  },
  getUserId: state => {
    return state.userId
  },
  getAccessToken: state => {
    return state.accessToken;
  },
  getIsGuide: state => {
    return state.isGuide
  },
  getMyName: state => {
    return state.myName
  },
};

const mutations = {
  setToken: (state, data) => {
    state.accessToken = data.accessToken
    state.refreshToken = data.refreshToken
  },
  getLogin: (state) => {
    // console.log(state.accessToken);
    // console.log(state.refreshToken);
    // console.log(response);
  },
  logOutData: (state) => {
    state.accessToken = null,
    state.refreshToken = null,
    state.isLogin = false
    state.isGuide = null
    
    // window.location.reload(true)
  },
  logOutDataWithoutRefresh: (state) => {
    state.token = null,
    state.isLogin = false
  },
  logInData: (state, data) => {
    console.log('로그인됨');
    state.isLogin = true
    state.userId = data
  },
  signRegister: (state) => {
    state.signRegister = true
  },
  signRegisterFalse: (state) => {
    state.signRegister = false
  },
  isItGuide: (state, data) => {
    state.isGuide = data
  },
  setMyName: (state, data) => {
    state.myName = data
  },
}

const actions = {
  GuideAction: async ({commit, state}) =>{
    try {
      const res = await getUserInfo(`Bearar ${state.accessToken}`)
      commit('isItGuide', res.data.result.state)
    } catch (err) {
      console.log(err);
    }
  },
  loginAction: async ({ commit, dispatch, state }, loginData) => {
    try {
      const response = await requestLogin(loginData);
      if (response.data.resultCode == 200) {
        alert('로그인 성공')
        await commit("logInData", loginData.id)
        await commit("setToken", response.data)
        await commit("setMyName", response.data.userName)
        await dispatch('GuideAction')
        router.push({name: "home"})
      } else {
        console.log(response.data.status);
        alert('로그인 실패')
      }
    } catch (error) {
      console.log(error.response);
      alert('잘못된 정보입니다.')
    }
  },
  signinAction: async ({ commit }, signinData) => {
    try {
      const response = await requestSignin(signinData);
      console.log(response);
      alert('회원가입이 완료되었습니다.')
    } catch (error) {
      alert('회원가입에 실패하였습니다.')
    }
  },

  confirmAction: async ({ commit, state, dispatch, getters }) => {
    try {
      const response = await getUserInfo(`Bearer ${state.accessToken}`);
      if (response.data.resultCode == 200) {
        await commit("logInData", response.data.result.id)
        await dispatch('GuideAction')
      } else {
        await commit("logOutData")
      }
    } catch (error) {
      commit("logOutData")
    }
  },

};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
