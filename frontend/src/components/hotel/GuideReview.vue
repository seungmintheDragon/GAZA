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
                <router-link to="/guideInfoDelete">내 정보 삭제</router-link>
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
                  class="active"
                  >리뷰조회</router-link
                >
              </li>
            </ul>
          </div>
        </div>
        <div class="col-lg-8">


          <div class="dashboard_common_table">
            <h3>리뷰조회</h3>
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
                <tbody v-for="rev in guidereview" :key="rev.reviewId">
                  <tr>
                    <td>{{ rev.reviewId }}</td>
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
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import axios from "@/api/http";
import { useStore } from "vuex";
import { onMounted, ref } from "vue";

export default {
  name: "GuideReview",
  setup() {
    const guidereview = ref([]);

    const store = useStore();
    const getDate = (date) => {
      const DAT = new Date(date);
      return (
        DAT.getFullYear() + "-" + (DAT.getMonth() + 1) + "-" + DAT.getDate()
      );
    };

    const getvalue = () => {
      const loginId = store.getters["accountStore/getUserId"];
      axios.get(`reviews/guide/id//${loginId}`).then((res) => {
        guidereview.value = res.data;
      });
    };

    onMounted(() => {
      getvalue();
    });

    return {
      guidereview,
      getDate
    };
  },
};
</script>
