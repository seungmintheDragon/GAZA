<template>
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
        pictureURL:'',
        pictureData:null,
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

      console.log(user);
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

<style>

</style>