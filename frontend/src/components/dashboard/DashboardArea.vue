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
                  <router-link to="/dashboard" class="active"   
                    ><i class="fas fa-list"></i>이용후기</router-link
                  >
                </li>
                <li>
                  <router-link to="/my-profile"
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
            <div class="dashboard_main_top">
              <div class="d-flex align-items-center justify-content-between">
                <div class="col-lg-4 col-md-6 col-sm-12 col-12">
                  <div class="form_search_date">
                    <div class="flight_Search_boxed date_flex_area">
                      <div class="Journey_date">
                        <p>From</p>
                        <input v-model="StartDate" type="date" />
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-lg-4 col-md-6 col-sm-12 col-12">
                  <div class="form_search_date">
                    <div class="flight_Search_boxed date_flex_area">
                      <div class="Journey_date">
                        <p>To</p>
                        <input  v-model="EndDate" type="date" />
                      </div>
                    </div>
                  </div>
                </div>

                <div>
                  <button @click="filter_date" class="btn btn_theme btn-lg">Filter</button>
                </div>
              </div>
            </div>
          </div>
          <hr />

          <div class="dashboard_common_table">
            <h3>내가 쓴 후기</h3>
            <div class="table-responsive-lg table_common_area">
              <table class="table">
                <thead>
                  <tr>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>별점</th>
                    <th>내용</th>
                  </tr>
                </thead>
                <tbody v-for="rev in review2" :key="rev.id">
                  <tr>
                    <td>{{ rev.userId}}</td>
                    <td>{{ getDate(rev.createdDate) }}</td>
                    <td>
                      <i
                        v-for="score in rev.score"
                        :key="score"
                        class="fas fa-sharp fa-solid fa-star"
                        style="color: yellow"
                      ></i>
                    </td>
                    <td>{{ rev.content }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="pagination_area">
            <ul class="pagination">
              <li v-if="currentpage !== 1" class="page-item">
                <a
                  style="cursor: pointer"
                  class="page-link"
                  @click="getValue(currentpage - 1)"
                  aria-label="Previous"
                >
                  <span aria-hidden="true">«</span>
                  <span class="sr-only">Previous</span>
                </a>
              </li>
              <li v-for="page in numberofpages" :key="page" class="page-item">
                <a
                  style="cursor: pointer"
                  class="page-link"
                  @click="getValue(page)"
                >
                  {{ page }}</a
                >
              </li>
              <a
                v-if="currentpage !== numberofpages"
                style="cursor: pointer"
                class="page-link"
                @click="getValue(currentpage + 1)"
                aria-label="Next"
              >
                <span aria-hidden="true">»</span>
                <span class="sr-only">Next</span>
              </a>
            </ul>
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
import { getUserInfo } from "../../../common/api/commonAPI";
import { reviewss } from "../../../common/api/commonAPI";
import { onMounted, reactive } from 'vue';
import { useStore } from 'vuex';

import { ref, computed } from "vue";
export default {
  name: "DashboardArea",
  components: {
    LogoutBtn,
    MyBookingOption,
    picturemodalVue,
  },
  setup() {
    const store= useStore();

    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/user/picture/";

    const state = reactive({
      info: {
        originName: '',
        first_name: '',
        last_name:'',
        pictureURL:'',
      },
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

    const loginID = store.getters["accountStore/getUserId"];

    const review1 = ref([]);
    const review2 = ref([])
    const numberofreviews = ref(0);
    const currentpage = ref(1);
    const limit = 5;
    const StartDate =ref("");
    const EndDate =ref("");

    const getDate = ( date ) => {
      const DAT = new Date(date)
      return DAT.getFullYear() +'-'+(DAT.getMonth()+1) +'-' +DAT.getDate()
    }

    const filter_date=() => {
      const [syear,smonth,sday] =StartDate.value.split('-');
      const SD = new Date(+syear,smonth-1,+sday);

      const [eyear,emonth,eday] =EndDate.value.split('-');
      const ED = new Date(+eyear,emonth-1,+eday);

      let newarray = []
      review1.value.filter( re =>{
        const reND = new Date(re.createdDate);
        const [nyear,nmonth,nday] =[reND.getFullYear(),(reND.getMonth()),reND.getDay()]
        const ND = new Date(+nyear,nmonth,+nday);
        
        if(ND >=SD && ND <=ED){
          newarray.push(re)
        }

      })

      review2.value = newarray
    }


    const getValue = async (page = currentpage.value) => {
      currentpage.value = page;
      try {
        const res = await reviewss(
         loginID
        );
        numberofreviews.value = res.data['length'];
        review1.value = res.data;
        review2.value = res.data;

      } catch (err) {
        console.log(err);
      }
    };
    getValue();

    const coputeReview = computed(() =>{
      return review,StartDate,EndDate
    })


    const numberofpages = computed(() => {
      return Math.ceil(numberofreviews.value / limit);
    });

    return {
      getValue,
      state,
      limit,
      numberofpages,
      currentpage,
      numberofreviews,
      review1,
      review2,
      StartDate,
      EndDate,
      coputeReview,
      filter_date,
      getDate,
      loginID 


    };
  },
};
</script>
