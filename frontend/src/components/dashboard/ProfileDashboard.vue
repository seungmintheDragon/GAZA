<template>
  <section id="dashboard_main_arae" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col-lg-4">
          <div class="dashboard_sidebar">
            <div class="dashboard_sidebar_user">
              <img :src="state.info.pictureURL" alt="img" />
              <h3>{{ state.info.originName }}</h3>
              <div>
                <picturemodalVue @pictureData="changePicture"/>
              </div>
            </div>
            <div class="dashboard_menu_area">
              <ul>
                <li>
                  <router-link to="/dashboard"
                    ><i class="fas fa-list"></i>이용후기</router-link
                  >
                </li>
                <li>
                  <router-link to="/my-profile" class="active"
                    ><i class="fas fa-user-circle"></i>내 정보 수정</router-link
                  >
                </li>
                <li>
                  <router-link to="/notification"
                    ><i class="fas fa-bell"></i>예약내역</router-link
                  >
                </li>
                <LogoutBtn />
              </ul>
            </div>
          </div>
        </div>
        <div class="col-lg-8">
          <div class="dashboard_common_table">
            <h3>내 정보 수정</h3>
            <div class="profile_update_form">
              <form id="profile_form_area" v-on:submit.prevent="updateMyPage">
                <div class="row">
                  <div class="col-lg-6">
                    <div class="form-group">
                      <label for="f-name">First name</label>
                      <input
                        type="text"
                        class="form-control"
                        id="f-name"
                        placeholder="Your Name"
                        v-model="state.info.first_name"
                      />
                    </div>
                  </div>
                  <div class="col-lg-6">
                    <div class="form-group">
                      <label for="l-name">Last name</label>
                      <input
                        type="text"
                        class="form-control"
                        id="l-name"
                        placeholder="Last name"
                        v-model="state.info.last_name"
                      />
                    </div>
                  </div>
                  <div class="col-lg-6">
                    <div class="form-group">
                      <label for="mail-address">Email</label>
                      <input
                        type="text"
                        class="form-control"
                        id="mail-address"
                        v-model="state.info.email"
                      />
                    </div>
                  </div>
                  <div class="col-lg-6">
                    <div class="form-group">
                      <label for="mobil-number">전화번호</label>
                      <input
                        type="text"
                        class="form-control"
                        id="mobil-number"
                        v-model="state.info.phone_number"
                      />
                    </div>
                  </div>
                  <div class="change_password_input_boxed">
                    <h3>비밀번호 변경</h3>
                    <div class="row">
                      <div class="col-lg-6">
                        <div class="form-group">
                          <input
                            type="password"
                            class="form-control"
                            placeholder="새 비밀번호"
                            v-model="state.info.newPassword"
                            :class="{ 'formerror': passwordc }"
                            @keyup="uppassword"
                            minlength="6"
                          />
                        </div>
                      </div>
                      <div class="col-lg-6">
                        <div class="form-group">
                          <input
                            type="password"
                            class="form-control"
                            placeholder="비밀번호 확인"
                            :class="{ 'formerror': passwordc }"
                            @keyup="uppassword"
                            v-model="passwordcheck"
                            minlength="6"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="d-flex justify-content-end mt-3">
            <button class="btn btn_theme" @click="save()">저장</button>
            <withdrawVue/>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import { useStore } from 'vuex';
import { updateUser, getUserInfo, changePassword } from "../../../common/api/commonAPI";
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import LogoutBtn from "@/components/dashboard/LogoutBtn.vue";
import MyBookingOption from "@/components/dashboard/MyBookingOption.vue";
import picturemodalVue from "../modal/picturemodal.vue";
import withdrawVue from "../modal/withdraw.vue";

export default {
  components: {
    LogoutBtn,
    MyBookingOption,
    picturemodalVue,
    withdrawVue,
  },


  setup() {
    const store = useStore();

    const router = useRouter();

    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/user/picture/";

    const passwordcheck = ref('');
    const passwordc =ref(true);

    const state = reactive({
      info: {
        originName: '',
        first_name: '',
        last_name:'',
        email:'',
        phone_number:'',
        pictureURL:'',
        pictureData:null,
        newPassword:'',
      },
    })

    onMounted(async () => {
      const accessToken = 'Bearer ' + store.getters['accountStore/getAccessToken']

      const user_info = await getUserInfo(accessToken);

      const user = user_info.data.result;

      passwordc.value=false;
      state.info.pictureURL=baseURL + user.picture;
      state.info.originName=user.name;
      state.info.first_name=user.name.substring(0,1);
      state.info.last_name=user.name.substring(1);
      state.info.email=user.email + '@' + user.email_domain;
      state.info.phone_number=user.phone_number;

    })

    const uppassword = function () {
        passwordc.value = false
    }

    const changePicture = function(pictureData) {
      console.log(pictureData);

      state.info.pictureData = pictureData;
    }

    const save = function () {
      const accessToken = 'Bearer ' + store.getters['accountStore/getAccessToken']

      const name = state.info.first_name + state.info.last_name;

      const email = state.info.email.split("@")[0];

      const email_domain = state.info.email.split("@")[1];

      const user_info = {
        name : name,
        email : email,
        email_domain : email_domain,
        phone_number : state.info.phone_number,
      }

      let payload = {
        picture: state.info.pictureData === null ? null : state.info.pictureData,
        userInfo: JSON.stringify(user_info)
      }

      console.log(payload);

      if(state.info.newPassword !== '' && passwordcheck.value !== '') {
          if((state.info.newPassword.length < 6) || (state.info.newPassword !== passwordcheck.value)) {
            alert("비밀번호를 확인해주세요.");
          } else {
            updateUser(payload, accessToken);
            changePassword(JSON.stringify({"password" : state.info.newPassword}), accessToken);
            alert("회원 정보와 비밀번호가 변경되었습니다.")
            router.go(0);
          }
      } else {
        updateUser(payload, accessToken);
        alert("회원 정보가 변경되었습니다.")
        router.go(0);
      }
    }

    return {
      store,
      state,
      withdrawVue,
      picturemodalVue,
      MyBookingOption,
      LogoutBtn,
      passwordcheck,
      passwordc,
      save,
      changePicture,
      uppassword
    };
  },
};

</script>
