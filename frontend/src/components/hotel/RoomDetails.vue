<template>
  <section id="tour_details_main" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col-3">
          <div class="dashboard_menu_area">
            <ul>
              <li>
                <router-link to="/room-details" class="active"
                  >내 정보 확인 및 수정</router-link
                >
              </li>
              <li>
                <router-link to="/guideInfoDelete"
                  >추천 장소 관리</router-link
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
          <div class="tour_details_leftside_wrapper">
            <div class="tour_details_heading_wrapper">
              <div></div>
            </div>
            <hr />

            <!--자기소개 form -->
            <div class="d-flex">
              <div class="ms-3">
                <div class="dashboard_common_table">
                  <h3>프로필 업로드</h3>
                  <div class="profile_update_form">
                    <form
                      id="profile_form_area"
                      v-on:submit.prevent="updateMyPage"
                    >
                      <input
                        type="file"
                        @change="selectFile"
                        id="pictureData"
                        ref="pictureData"
                        accept="image/*"
                      />
                      <div class="row">
                        <div class="col-lg-20">
                          <div class="form-group">
                            <label for="f-one-liner">한 줄 소개</label>
                            <input
                              type="text"
                              class="form-control"
                              id="f-one-liner"
                              v-model="guide.info.onelineIntroduction"
                            />
                          </div>
                        </div>
                        <div class="col-lg-20">
                          <div class="form-group">
                            <label for="l-price">Price</label>
                            <input
                              type="text"
                              class="form-control"
                              id="l-price"
                              v-model="guide.info.price"
                            />
                          </div>
                        </div>
                        <div class="col-lg-6">
                          <div class="form-group">
                            <label for="country">Country</label>
                            <input
                              type="text"
                              class="form-control"
                              id="country"
                              v-model="guide.info.country"
                            />
                          </div>
                        </div>
                        <div class="col-lg-6">
                          <div class="form-group">
                            <label for="city">City</label>
                            <input
                              type="text"
                              class="form-control"
                              id="city"
                              v-model="guide.info.city"
                            />
                          </div>
                        </div>
                        <div class="change_password_input_boxed">
                          <h3>Introduction</h3>
                          <div class="row">
                            <div class="col-lg-20">
                              <div class="form-group">
                                <textarea
                                  type="text"
                                  class="form-control"
                                  placeholder="Introduction"
                                  v-model="guide.info.introduction"
                                ></textarea>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="d-flex justify-content-end mt-3">
                        <button class="btn btn_theme">수정</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>

            <div class="new_main_news_box">
              <br />
              <h3 style="font-weight: bold">가이드의 추천 명소 관리</h3>
              <div class="row">
                <div class="col-lg-4 col-md-6 col-sm-12 col-12">
                  <div class="news_item_boxed">
                    <form
                      id="profile_form_area"
                      v-on:submit="locationRegister"
                    >
                      <input
                        type="file"
                        @change="getFileName"
                        id="locPicture"
                        ref="pictureData2"
                        accept="image/*"
                      />
                      <div class="col-lg-20">
                        <div class="form-group">
                          <label for="f-one-liner">장소 이름</label>
                          <input
                            type="text"
                            class="form-control"
                            id="f-one-liner"
                            v-model="location.info.name"
                          />
                        </div>
                      </div>
                      <div class="col-lg-20">
                        <div class="form-group">
                          <label for="f-one-liner">주소</label>
                          <input
                            type="text"
                            class="form-control"
                            id="f-one-liner"
                            v-model="location.info.address"
                          />
                        </div>
                      </div>
                      <div class="col-lg-20">
                        <div class="form-group">
                          <label for="f-one-liner">카테고리</label>
                          <!-- <input
                            type="text"
                            class="form-control"
                            id="f-one-liner"
                            v-model="location.info.categoryName"
                          /> -->
                          <select class="form-select" aria-label="Default select example" v-model="location.info.categoryName">
                            <option v-for="(idx) in codeNameList" :value="idx.description">{{idx.description}}</option>
                        </select>
                        </div>
                      </div>

                      <button class="btn btn_theme btn_sm">submit</button>
                    </form>
                  </div>
                </div>
              </div>
            </div>

            <!-- 가이드 여행 테마 등록 -->
            <div class="new_main_news_box mybox">
              <br />
              <h3 style="font-weight: bold">가이드의 여행 테마</h3>
              <div class="row">
                <thema-box></thema-box>
              </div>
            </div>

            <!--start 가이드 언어-->
            <div class="new_main_news_box mybox">
              <br />
              <h3 style="font-weight: bold">가이드의 사용 가능한 언어</h3>
              <div class="row">
                <language-box></language-box>
              </div>
            </div>
          </div>
          <!-- end 가이드 언어-->
          <hr />
          <h3 style="font-weight: bold">상담 불가 날짜/시간 설정</h3>
          <br />
          <!--start 상담 불가능 날짜 form -->
          <div class="row d-flex justify-content-start">
            <div class="col-lg-4">
              <h5 style="color: #15d4cd">Choose a Date</h5>
              <br />
              <div class="form_search_date">
                <div class="flight_Search_boxed date_flex_area">
                  <div class="Journey_date">
                    <form v-on:submit="register_date">
                      <input type="date" v-model="date_info"  style="margin-bottom: 30px;"/>
                      <button class="btn btn_theme btn_sm me-1 mb-2">
                        submit
                      </button>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            <!--end 상담 불가능 날짜 form -->

            <!--start 시간대 설정 form -->
            <div class="col-lg-4 ms-5">
              <form v-on:submit.prevent="impossibleTime">
                <h5 style="color: #15d4cd">Pick a time</h5>
                <div class="tour_details_boxed"  style="margin-bottom: 30px;">
                  <h5>시작시간</h5>
                  <input type="time" v-model="startTime" />
                  <h5>종료 시간</h5>
                  <input type="time" v-model="endTime" />
                </div>
                <button class="btn btn_theme btn_sm">submit</button>
              </form>
            </div>
            <!--end 시간대 설정 form -->
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";
import { ref } from "vue";
import {
  myPageUpdate,
  myPageShow,
  registerDate,
  guideLangRegister,
  guideLocationRegister,
  codeList
} from "../../../common/api/commonAPI.js";
import LanguageBox from "@/components/hotel/guideSettings/LanguageBox";
import ThemaBox from "@/components/hotel/guideSettings/ThemaBox";
import router from "@/router";

export default {
  name: "RoomDetails",
  components: {
    LanguageBox,
    ThemaBox
  },
  setup() {
    const store = useStore();

    const date_info = ref(null);
    const pictureData = ref(null);
    const pictureData2 = ref(null);

    const startTime = ref();
    const endTime = ref();

    const codeNameList = ref();

    //가이드 마이페이지 정보
    const guide = reactive({
      info: {
        city: "",
        country: "",
        introduction: "",
        onelineIntroduction: "",
        price: 0,
      },
    });

    const guideFile = ref(null);

    const locPicture = ref(null);

    const location = reactive({
      info: {
        userId: "",
        name: "",
        address: "",
        categoryName: "",
      },
    });

    const language = ref();

    const selectFile = function () {
      guideFile.value = pictureData.value.files[0];
      console.log(guideFile);
    };

    const getFileName = function () {
      locPicture.value = pictureData2.value.files[0];
      console.log(locPicture);
    };

    //마이페이지 가이드 정보 조회
    const getInfo = async (loginId) => {
      const response = await myPageShow(loginId); //call axios
      guide.info = response.data;
    };

    onMounted(() => {
      const isLogin = store.getters["accountStore/getIsLogin"];
      if(isLogin == false){
        alert("로그인 후 이용하세요");
        router.push({name:"home"});
      }

      const loginId = store.getters["accountStore/getUserId"];

      getInfo(loginId); //수정을 위해 미리 가이드 정보 띄워놓기

      const param = "추천장소분류";
      getCodeList(param);

    });

    //상담 불가능 시간대 설정
    const impossibleTime = function () {
      const start_time = startTime.value;
      const end_time = endTime.value;

      const set_time_info = {
        userId: store.getters["accountStore/getUserId"],
        timeStart: `${start_time}:00`,
        timeEnd: `${end_time}:00`,
      };

      registerTime(set_time_info); //call axios
    };

    //마이페이지 수정
    const updateMyPage = function () {
      const info = guide.info;

      const loginId = store.getters["accountStore/getUserId"];

      const request = {
        city: info.city,
        country: info.country,
        introduction: info.introduction,
        onelineIntroduction: info.onelineIntroduction,
        price: info.price,
        userId: loginId,
      };

      const payload = {
        guide: JSON.stringify(request),
        picture: guideFile.value,
      };

      console.log(payload);

      myPageUpdate(payload); //call axios

      alert("가이드 정보가 수정되었습니다.");
    };

    //가이드 상담 불가능 날짜 등록
    const register_date = function () {
      const loginId = store.getters["accountStore/getUserId"];

      const date = date_info.value;

      const request = {
        day: date,
        userId: loginId,
      };
      registerDate(request); //call axios
    };

    //가이드 사용 가능한 언어 등록
    const langRegister = function () {
      const request = language.value;
      const payload = {
        languageName: request,
        loginId: store.getters["accountStore/getUserId"],
      };

      guideLangRegister(payload); //call axios
    };

    //가이드 추천 장소 등록
    const locationRegister = function () {
      location.info.userId = store.getters["accountStore/getUserId"];

      const request = {
        categoryName:location.info.categoryName,
        address:location.info.address,
        loginId:location.info.userId,
        name:location.info.name
      }

      //console.log(locPicture)
      const payload = {
        location: JSON.stringify(request),
        uploadFile: locPicture.value,
      };

      guideLocationRegister(payload); //call axios
    };


    //코드 목록 리스트
    const getCodeList = async(lang) =>{
      const response = await codeList(lang);
      codeNameList.value = response.data;
      console.log(codeNameList.value);
    };

    return {
      impossibleTime,
      store,
      updateMyPage,
      guide,
      getInfo,
      register_date,
      date_info,
      endTime,
      startTime,
      selectFile,
      pictureData,
      langRegister,
      language,
      locationRegister,
      location,
      getFileName,
      locPicture,
      guideFile,
      pictureData2,
      guideLocationRegister,
      codeNameList,
      getCodeList
    };
  },
};
</script>
<style scoped>

.mybox {
  width:100%;
}
</style>
