<template>
  <div>
    <button
      @click="modalToggle"
      class="btn btn_theme ms-2"
    >
      회원탈퇴
    </button>

    <div
      ref="modal"
      class="modal fade"
      :class="{ show: active, 'd-block': active }"
      tabindex="-1"
      role="dialog"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-body logout_modal_content">
            <div class="btn_modal_closed">
              <button
                type="button"
                data-bs-dismiss="modal"
                @click="modalToggle"
                aria-label="Close"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
            <h3>
              정말 <br />
              회원탈퇴 하시겠습니까?
            </h3>
            <div class="logout_approve_button">
              <button data-bs-dismiss="modal" class="btn btn_theme btn_md" @click="del" router-link to="/">
                네
              </button>
              <button
                data-bs-dismiss="modal"
                class="btn btn_border btn_md"
                @click="modalToggle"
              >
                아니요
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {  onMounted } from "vue";
import { deleteUser } from "../../../common/api/commonAPI";
import { useStore } from "vuex";
import router from '@/router';
export default {
  name: "withdraw",
  data() {
    return {
      active: false,
    };
  },
  methods: {
    modalToggle() {
      const body = document.querySelector("body");
      this.active = !this.active;
      this.active
        ? body.classList.add("modal-open")
        : body.classList.remove("modal-open");
    },
  },
  setup() {
    const store = useStore();


    const del = () => {
      const accessToken = 'Bearer ' +store.getters["accountStore/getAccessToken"];
      console.log(accessToken);
      deleteUser(accessToken);
      store.commit("accountStore/logOutData");
      router.push({name: 'home'})
    };
    return {
      del,
    };
  },
};
</script>
