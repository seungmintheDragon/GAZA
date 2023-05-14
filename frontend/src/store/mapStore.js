import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    routes: [
      // { title: '할 일1', completed: false },
      // { title: '할 일2', completed: false },
    ],
  },
  getters: {
    allRoutesCount(state) {
      return state.routes.length;
    },
    completedRoutesCount(state) {
      return state.routes.filter((route) => {
        return route.completed === true;
      }).length;
    },
    unCompletedRoutesCount(state) {
      return state.routes.filter((route) => {
        return route.completed === false;
      }).length;
    },
  },
  mutations: {
    //////////////////////////// Todo List start //////////////////////////////////
    CREATE_ROUTE(state, routeItem) {
      // console.log('mutation CREATE_TODO Call');
      // console.log(state);
      // console.log(todoItem);
      state.routes.push(routeItem);
    },
    DELETE_ROUTE(state, routeItem) {
      const index = state.routes.indexOf(routeItem);
      state.routes.splice(index, 1);
    },
    UPDATE_ROUTE_STATUS(state, routeItem) {
      // console.log(state);
      // console.log(todoItem);
      state.routes = state.routes.map((route) => {
        if (route === routeItem) {
          return {
            // title: todoItem.title,
            ...route,
            completed: !routeItem.completed,
          };
        }
        return route;
      });
    },
    //////////////////////////// Todo List end //////////////////////////////////
  },
  actions: {
    //////////////////////////// Todo List start //////////////////////////////////
    // createTodo(context, todoItem) {
    //   console.log(context);
    //   console.log('actions createTodo Call');
    //   context.commit('CREATE_TODO', todoItem);
    // },
    // destructuring 활용
    createRoute({ commit }, todoItem) {
      commit("CREATE_ROUTE", todoItem);
    },
    deleteRoute({ commit }, todoItem) {
      commit("DELETE_ROUTE", todoItem);
    },
    updateRouteStatus({ commit }, todoItem) {
      commit("UPDATE_ROUTE_STATUS", todoItem);
    },
    //////////////////////////// Todo List end //////////////////////////////////
  },
  modules: {},
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
});
