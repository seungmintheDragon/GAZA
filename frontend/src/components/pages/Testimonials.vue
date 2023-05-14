<template>
  <section id="tour_details_main" class="section_padding">
    <div class="container">
      <div class="row">
        <div class="col">
          <br />
          <div class="row d-flex justify-content-center">
            <div class="col-lg-4 me-2">
              <h3 style="color: #15d4cd">상담날짜</h3>
              <br />
              <div>
                <div class="flight_Search_boxed date_flex_area">
                  <div class="Journey_date">
                    <input type="date" name = "consult-date" @change="checkImpossibleTime" v-model="state.info.consultingDate"/>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-lg-4 ms-2">
              <h3 style="color: #15d4cd">시간대 설정</h3>
              <div class="reser_boxed">
                <!--  data-bs-toggle="button" aria-pressed="false" autocomplete="off" -->
                <div class="btn-group" role="group" v-for="index in 4" :key="index">
                  <button type="button" :class="{ 'disabled' : ((state.today===state.selectedDate) && ((index-1)*6+btn_index-1 <= state.now_hour)) || state.impossibleTime.includes((index-1)*6+btn_index-1)}"
                   class="btn btn_theme btn_sm me-1 mb-2" :value=(index-1)*6+btn_index-1  @click="setTime" v-for="btn_index in 6" :key="btn_index"> {{ ((index-1)*6+btn_index-1 < 10) ? '0' + ((index-1)*6+btn_index-1) : (index-1)*6+btn_index-1 }}</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-8 offset-lg-2 mt-5">
          <div class="section_heading_center">
            <h3 style="color: #15d4cd">여행 정보 입력</h3>
          </div>
          <div class="contact_form">
            <div class="row">
              <div class="col-lg-4">
                <div class="form-check">
                  <input class="form-check-input" name = "disabled" id="disabled" type="checkbox" v-model="state.info.withDisabled"/>
                  <label class="form-check-label" for="disabled">장애 여부</label>
                </div>
              </div>
              <div class="col-lg-4">
                <div class="form-check">
                  <input class="form-check-input" name = "with-baby" id = "with-baby" type="checkbox" v-model="state.info.withChildren"/>
                  <label class="form-check-label" for="with-baby">유아 동반</label>
                </div>
              </div>
              <div class="col-lg-4">
                <div class="form-check">
                  <input class="form-check-input" name = "with-weak-or-elderly" id = "with-weak-or-elderly" type="checkbox" v-model="state.info.withElderly"/>
                  <label class="form-check-label" for="with-weak-or-elderly">노약자 동반</label>
                </div>
              </div>
              <div class="row mt-4 mb-4">
                <div class="col-lg-6">
                  <div class="input-group">
                    인원 : &nbsp; <input min="0" name = "head-count" type="number" v-model="state.info.numberOfPeople"/>
                  </div>
                </div>
                <div class="col-lg-6">여행시작날짜:
                  <input type="date" name = "travel-start-date" v-model="state.info.travel_start_date" /> &nbsp;
                  <select required name="travel-start-time" id="travel-start-time" v-model="state.info.travel_start_time">
                    <option :value=index v-for="index in 24" :key="index">{{ index }}시</option>
                  </select>
                </div>
                <div class="col-lg-6"></div>
                <div class="col-lg-6">여행종료날짜: 
                  <input type="date" name = "travel-end-date" v-model="state.info.travel_end_date" /> &nbsp;
                  <select required name="travel-end-time" id="travel-end-time" v-model="state.info.travel_end_time">
                    <option :value=index v-for="index in 24" :key="index">{{ index }}시</option>
                  </select>
                </div>
              </div>
              <div style="font-weight: bold">특이사항</div>
              <div class="col-lg-12 mt-4">
                <div class="form-group">
                  <textarea class="form-control bg_input" name = "note"  rows="6" v-model="state.info.note"></textarea>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="section_heading_center mt-4">
          <router-link to="/complete">
            <button type="button" class="btn btn_theme btn_md" @click="reserveConsulting()">예약하기</button>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import { onMounted, reactive } from 'vue';
import { useStore } from 'vuex';
import { useRoute } from 'vue-router'
import { reserve, getImpossibleTime } from '../../../common/api/commonAPI.js'

export default {
  setup() {
    const store = useStore();

    const route = useRoute();

    const state = reactive({
      info: {
        consultingDate: '',
        consultingTime: '',
        withDisabled:'',
        withChildren:'',
        withElderly:'',
        numberOfPeople:'',
        travel_start_date:'',
        travel_start_time:'',
        travel_end_date:'',
        travel_end_time:'',
        note:'',
      },
      today: '',
      selectedDate: '',
      now_hour : '',
      impossibleTime : [],
    })

    onMounted(() => {
      state.info.withDisabled=false
      state.info.withChildren=false
      state.info.withElderly=false
    
      var now_utc = Date.now() // 지금 날짜를 밀리초로
      // getTimezoneOffset()은 현재 시간과의 차이를 분 단위로 반환
      var timeOff = new Date().getTimezoneOffset()*60000; // 분단위를 밀리초로 변환
      // new Date(now_utc-timeOff).toISOString()은 '2022-05-11T18:09:38.134Z'를 반환
      var today = new Date(now_utc-timeOff).toISOString().split("T")[0];
      state.info.consultingDate = today;
      state.selectedDate = today;
      state.today = today
      state.now_hour = new Date(now_utc-timeOff).toISOString().split("T")[1].split(":")[0];
      const dates =  document.querySelectorAll('input[type="date"]');
      dates.forEach((date) => {
        date.setAttribute("min", today);
      })
    })

    const checkImpossibleTime = async function() {
      state.selectedDate = event.currentTarget.value;
      
      try{
        const res = await getImpossibleTime(route.params.guideId, event.currentTarget.value);
        state.impossibleTime = res.data;
      } catch (err) {
        console.log(err);
      }
    }
    
    // .setAttribute("min", today);

    const setTime = function() {
      state.info.consultingTime = event.currentTarget.value < 10 ? '0' + event.currentTarget.value : event.currentTarget.value;
      focusBtn();
      console.log(state.today + "   " + state.selectedDate);
    }

    const focusBtn = function() {
      const active_btn = document.querySelector(".btn-group > .active");

      if(active_btn !== null){
        active_btn.className = 'btn btn_theme btn_sm me-1 mb-2';
      }

      event.currentTarget.className = 'btn btn_theme btn_sm me-1 mb-2 active'
    }

    const reserveConsulting = function () {
      console.log(state.info);

      const travel_start_time = state.info.travel_start_time < 10 ? '0' + state.info.travel_start_time : state.info.travel_start_time; 

      const travel_end_time = state.info.travel_end_time < 10 ? '0' + state.info.travel_end_time : state.info.travel_end_time;

      const reservation_info = {
        userId : store.getters['accountStore/getUserId'],
        guideId : route.params.guideId,
        consultingDate: `${state.info.consultingDate}T${state.info.consultingTime}:00:00.000Z`,
        travelStartDate: `${state.info.travel_start_date}T${travel_start_time}:00:00.000Z`,
        travelEndDate: `${state.info.travel_end_date}T${travel_end_time}:00:00.000Z`,
        numberOfPeople:state.info.numberOfPeople,
        withDisabled: state.info.withDisabled ? 1 : 0,
        withChildren: state.info.withChildren ? 1 : 0,
        withElderly: state.info.withElderly ? 1 : 0,
        note:state.info.note,
      }
      
      reserve(reservation_info);
    }

    return {
      store,
      state,
      setTime,
      reserveConsulting,
      focusBtn,
      checkImpossibleTime,
    };
  },
}
</script>
<style scoped>
.active{
  background-color: black;
  color: white;
}
button.disabled {
  background-color: gray;
  color: white;

}
</style>