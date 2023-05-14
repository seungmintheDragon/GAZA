import { requestGuideRegisterList, allowGuideRequest, rejectGuideRequest } from "../../common/api/commonAPI";

const state = {
    registerGuideList : []
};

const getters = {
  getRegisterGuideList: state => {
    return state.registerGuideList
  }
};

const mutations = {
  setRegisterGuideList: (state, list) => {
    state.registerGuideList = list
  }
}

const actions = {
  getRegisterGuideListAction: async ({ commit }) => {
    try {
        const response = await requestGuideRegisterList();

        const list = response.data.result;
        
        console.log(list);
        console.log('리스트 읽어오기 성공');
        commit('setRegisterGuideList', list)
    } catch (error) {
        console.log(error);
    }
  },
  allowGuideRequest: async ({}, id) => {
    try {
        const response = await allowGuideRequest({"id" : id});

        console.log('가이드 신청 승인');
    } catch (error) {
        console.log(error);
    }
  },
  rejectGuideRequest: async ({}, id) => {
    try {
        const response = await rejectGuideRequest({"id" : id});
        
        console.log('가이드 신청 거절');
    } catch (error) {
        console.log(error);
    }
  }
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
