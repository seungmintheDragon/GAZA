<template>
  <section id="dashboard_main_arae" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col-lg-4">
          <div class="dashboard_sidebar">
            <div class="dashboard_sidebar_user">
              <img :src="state.info.pictureURL" alt="img" />
              <h3>{{ state.info.originName }}</h3>
            </div>
            <div class="dashboard_menu_area">
              <ul>
                <li>
                  <router-link to="/dashboard"
                    ><i class="fas fa-list"></i>이용후기</router-link
                  >
                </li>
                <li>
                  <router-link to="/my-profile"
                    ><i class="fas fa-user-circle"></i>내 정보 수정</router-link
                  >
                </li>
                <li>
                  <router-link to="/notification" class="active"
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
            <div class="write_your_review_wrapper">
              <h3 class="heading_theme">후기작성</h3>
              <div class="write_review_inner_boxed">
                <form @submit.prevent="upreview" id="news_comment_form">
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-froup">
                        <input
                          type="text"
                          class="form-control bg_input"
                          :value="route.params.name"
                          readonly
                        />
                        
                      </div>
                    </div>
                    <div class="col-lg-6">
                      <select class="star form-select form-control" required v-model="state.form.score">
                        <option value="5" class="star 5">&#xf005; &#xf005; &#xf005; &#xf005; &#xf005;</option>
                        <option value="4" class="star 4">&#xf005; &#xf005; &#xf005; &#xf005;</option>
                        <option value="3" class="star 3">&#xf005; &#xf005; &#xf005;</option>
                        <option value="2" class="star 2">&#xf005; &#xf005;</option>
                        <option value="1" class="star 1">&#xf005;</option>
                      </select>
                    </div>
                    <div class="col-lg-12">
                      <div class="form-froup">
                        <textarea
                          rows="6"
                          placeholder="리뷰를 작성해 주세요"
                          class="form-control bg_input"
                          v-model="state.form.content"
                          required
                        ></textarea>
                      </div>
                        <button class="btn btn_theme btn_md">
                          제출
                        </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      
      </div>
    </div>
  </section>
</template>
<script>
import LogoutBtn from "@/components/dashboard/LogoutBtn.vue";
import MyBookingOption from "@/components/dashboard/MyBookingOption.vue";
import picturemodalVue from "../modal/picturemodal.vue";
import { useRoute,useRouter } from 'vue-router';
import { uploadReview } from "../../../common/api/commonAPI";
import { reactive, onMounted } from 'vue';
import { useStore} from 'vuex';
import { getUserInfo } from "../../../common/api/commonAPI";

export default {
  name: "ReviewDashboard",
  components: {
    LogoutBtn,
    MyBookingOption,
    picturemodalVue,
  },

  setup(){
    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/user/picture/";

    const route = useRoute();
    const router = useRouter();

    const state = reactive({
      info: {
        originName: '',
        first_name: '',
        last_name:'',
        pictureURL:'',
      },
      form:{
        content :"",
        score : "",
        reservationId :""
      }
    })

    onMounted(async () => {
      const accessToken = 'Bearer ' + store.getters['accountStore/getAccessToken']

      const user_info = await getUserInfo(accessToken);

      const user = user_info.data.result;

      state.info.pictureURL=baseURL + user.picture;
      state.info.originName=user.name;
      state.info.first_name=user.name.substring(0,1);
      state.info.last_name=user.name.substring(1);

      console.log(user);
    })

    const store = useStore();
    const loginID = store.getters["accountStore/getUserId"];

    const upreview = async () => {
      console.log({
          content : state.form.content,
          score : state.form.score,
          reservationId : route.params.id
        });
      try{
        const res = await uploadReview({
          content : state.form.content,
          score : state.form.score,
          reservationId : route.params.id
        });
        console.log(res.data);
        alert("후기 작성이 완료되었습니다.");
        router.push(
          {
            name:"notification"
          }
        )
      }catch(err){

        console.log(err);
      }
    }

    const moveBefore =() => {
      router.push({
        name : "notification"
      })
    }
    

    return{
      route,
      upreview,
      state,
      moveBefore ,
      loginID

    }

  }
};
</script>
<style>
    .star{
        font-family: 'Font Awesome 5 Free';
        font-weight : 900;
    }
</style>
