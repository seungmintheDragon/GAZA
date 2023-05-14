<template>
  <section id="tour_details_main" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col">
          <div class="tour_details_leftside_wrapper">
            <div class="tour_details_heading_wrapper">
              <div>
              </div>
            </div>
            <hr />
            <form class="d-flex">
              <img
                style="border-radius: 50%"
                :src="baseURL+guideInfo.picture"
                alt="img"
                height="100"
                width="100"
              />
              <div class="ms-3">
                <h3>{{ guideInfo.name }}</h3>
                <br />
                <h5>{{ guideInfo.country }} / {{ guideInfo.city }} / {{ guideInfo.gender }}</h5>
                <span v-for="data in themaInfo" :key="data"> <span v-if="data != null">#{{ data }} &nbsp; </span> </span>
                <div>{{ guideInfo.introduction }}</div>
              </div>
            </form>
            <div class="d-flex mt-2 justify-content-end" v-if="loginId != guideInfo.id && isLogin">
              <router-link
                :to="{ name: 'testimonials', params: { guideId: $route.params.guideId } }"
              >
                <button class="me-2 btn btn_theme btn-lg">예약하기</button>
              </router-link>
            </div>

            <!--start 추천장소 -->
            <div class="new_main_news_box">
              <br />
              <h3 style="font-weight: bold">가이드의 추천 명소</h3>

            <swiper :slides-per-view="4"  :space-between="20" :pagination="{ clickable: true }">
              <swiper-slide v-for="loc in recommendInfo" :key="loc.name">
                  <div class="news_item_boxed">
                    <div class="news_item_img">
                      <img :src="baseURLregi+loc.picture" alt="img" height="280" width="450"/>
                    </div>
                    <div class="news_item_content">
                      <h3>
                        {{ loc.name }}
                      </h3>
                      <p>{{ loc.address }}</p>
                    </div>
                  </div>
              </swiper-slide>
            </swiper>
                
              <!--end 추천장소 -->
            </div>
          </div>
          <hr />

          <h3 style="font-weight: bold">리뷰</h3>
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
                <tbody v-for="rev in guideReview" :key="rev.id">
                  <tr v-if="!isReview"></tr>
                  <tr v-else>
                    <td>{{ rev.userId }}</td>
                    <td>{{ getDate(rev.createdDate) }}</td>
                    <td>
                      <i
                        v-for="score in rev?.score"
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
                <a style="cursor: pointer" class="page-link" @click="getValue(page)"> {{ page }}</a>
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
import { reviewss, guideDetail } from "../../../common/api/commonAPI";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/swiper-bundle.css";
import { ref, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "@/api/http";
import { useStore } from "vuex";

export default {

  components: {
    Swiper,
    SwiperSlide,
  },

  setup() {
    const store = useStore();
    const isLogin = store.getters["accountStore/getIsLogin"];
    const loginId = store.getters["accountStore/getUserId"];

    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/guide/mypage/";
    const baseURLregi = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/guide/location/";
    const route = useRoute();
    const review = ref([]);
    const numberofreviews = ref(0);
    const currentpage = ref(1);
    const limit = 5;
    const StartDate = ref("");
    const EndDate = ref("");

    const guideInfo = ref([]);
    const recommendInfo = ref([]);
    const themaInfo = ref([]);

    const guideReview = ref([]);
    const isReview = ref(false);

    const router = useRouter();

    const getDate = (date) => {
      const DAT = new Date(date);
      return DAT.getFullYear() + "-" + (DAT.getMonth() + 1) + "-" + DAT.getDate();
    };




    const getValue = async (page = currentpage.value) => {
      currentpage.value = page;
      console.log(currentpage.value)
      try {
        const res = await reviewss(`?_sort=id&_order=desc&_page=${page}&_limit=${limit}`);
        numberofreviews.value = res.data["length"];
        review.value = res.data;
      } catch (err) {
        console.log(err);
      }
    };
    getValue();

    const coputeReview = computed(() => {
      return review, StartDate, EndDate;
    });

    const numberofpages = computed(() => {
      return Math.ceil(numberofreviews.value / limit);
    });

    const MoveReser = (id) => {
      router.push({
        name: "about",
        params: {
          id: id,
        },
      });
    };

    onMounted(() => {
      const guideId = parseInt(route.params.guideId);
      console.log(guideId);

      axios.get(`/guides/${guideId}`).then((res) => {
        guideInfo.value = res.data;
        recommendInfo.value = res.data.guideLocationList;
        themaInfo.value = res.data.thema;
      });

      try {
        axios.get(`/reviews/guide/${guideId}`).then((res) => {
          console.log(res);
          if (res.data.status == 500) {
            guideReview.value = [{'id':0, 'name':'리뷰 없음'}]
          } else {
            guideReview.value = res.data;
            isReview.value = true
            console.log(guideReview.value)
          }
        });
      } catch (err) {
        guideReview.value = [{'id':0, 'name':'리뷰 없음'}]
        console.log(guideReview);
        console.log(err);
      }
      


    });

    return {
      isLogin,
      getValue,
      limit,
      numberofpages,
      currentpage,
      numberofreviews,
      review,
      StartDate,
      EndDate,
      coputeReview,
      getDate,
      router,
      MoveReser,
      guideInfo,
      recommendInfo,
      themaInfo,
      guideReview,
      isReview,
      baseURL,
      baseURLregi,
      loginId
    };
  },
};
</script>
