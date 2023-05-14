<template>
  <section id="tour_details_main" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col-3">
          <div class="dashboard_menu_area">
            <ul>
              <li>
                <router-link to="/room-details"
                  >내 정보 확인 및 수정</router-link
                >
              </li>
              <li>
                <router-link to="/guideInfoDelete" class="active"
                  >내 정보 삭제</router-link
                >
              </li>
              <li>
                <router-link
                  :to="{
                    name: 'guide_schedule',
                  }"
                  >예약내역 확인</router-link
                >
              </li>

              <li>
                <router-link
                  :to="{
                    name: 'guide_review',
                  }"
                  >리뷰조회</router-link
                >
              </li>
            </ul>
          </div>
        </div>

        <div class="col">
          <!-- 추천장소 조회 및 삭제 -->

          <div class="new_main_news_box">
            <h3 style="font-weight: bold">가이드의 추천 명소 관리</h3>
            <br />
            <div class="row">
              <div
                class="col-lg-4 col-md-6 col-sm-12 col-12"
                v-for="loc in recommendLoc"
                :key="loc.locationId"
              >
                <div class="news_item_boxed">
                  <form
                    id="profile_form_area"
                    v-on:submit.prevent="locationdelete(loc.locationId)"
                  >
                    <div class="news_item_img">
                      <img :src="baseURL + loc.picture" alt="img" />
                    </div>
                    <div class="news_item_content">
                      <h3>
                        {{ loc.name }}
                      </h3>
                      <p>{{ loc.address }}</p>

                      <!--end 추천장소 -->
                    </div>
                    <button class="btn btn_theme btn_sm" >Delete</button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import axios from "@/api/http";
import { useStore } from "vuex";
import { ref, onMounted, onUpdated } from "vue";
import { locdel } from "../../../common/api/commonAPI.js";
import { LanguageBox } from "@/components/hotel/guideSettings/LanguageBox";
export default {
  name: "GuideInfoDelete",
  setup() {
    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/guide/location/";

    const recommendLoc = ref([]);

    const store = useStore();

    const getvalue = () => {
      const loginId = store.getters["accountStore/getUserId"];
      axios.get(`guides/location/${loginId}`).then((res) => {
        recommendLoc.value = res.data;
      });
    };
    onMounted(() => {
      getvalue();
    });
    onUpdated(() => {
      getvalue();
    });

    //가이드 추천 장소 삭제
    const locationdelete = function (recommendId) {
      const loginId = store.getters["accountStore/getUserId"];
      locdel(loginId, recommendId);
    };

    return {
      recommendLoc,
      store,
      locationdelete,
      location,
      baseURL
    };
  },
};
</script>
<style scoped></style>
