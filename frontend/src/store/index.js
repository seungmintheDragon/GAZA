import { createStore } from "vuex";
import accountStore from "./accountStore";
import createPersistedState from "vuex-persistedstate";
import adminStore from "./adminStore";


export default createStore({
  modules: {
    accountStore,
    adminStore,
  },
  plugins: [
    createPersistedState({
      paths: ["accountStore",],
    })
  ]
});