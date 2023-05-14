<template>
    <div class="container">
      <h3 class="main-text mb-5">가자의 가이드가 되어보세요!</h3>
      <div class="row">
        <div class="vendor_form">
          <div class="tour_booking_form_box">
            <form
              id="tour_bookking_form_item"
              v-on:submit.prevent="register"
              enctype="multipart/form-data"
            >
              <div class="row">
                <div class="col-lg-2 subject-text">신분증</div>
                <div class="col-lg-6">
                  <div class="form-group">
                    <input
                      type="file"
                      class="custom-file-input"
                      placeholder="신분증"
                      id="file1"
                      ref="img1"
                      @change="selectFile1"
                    />
                    <label class="custom-file-label" for="신분증">Choose file</label>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-lg-2 subject-text">체류증명서류</div>
                <div class="col-lg-6">
                  <div class="form-group">
                    <input
                      type="file"
                      class="custom-file-input"
                      placeholder="체류증명서류"
                      id="file2"
                      ref="img2"
                      @change="selectFile2"
                    />
                    <label class="custom-file-label" for="체류증명서류">Choose file</label>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-lg-2 subject-text">가이드 자격증</div>
                <div class="col-lg-6">
                  <div class="form-group">
                    <input
                      type="file"
                      class="custom-file-input"
                      placeholder="가이드 자격증"
                      id="file3"
                      ref="img3"
                      @change="selectFile3"
                    />
                    <label class="custom-file-label" for="가이드 자격증">Choose file</label>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="subject-text">파트너 약관</div>
                <textarea
                  class="form-control"
                  id="floatingTextarea2"
                  style="height: 300px; padding: 10px"
                  rows="10"
                  readonly
                >
제3조 (여행계약의 당사자 및 당사의 지위)
여행자와 여행계약을 체결하고 가이드서비스를 제공하는 법적 주체는 가이드이며, 당사는 플랫폼을 통하여 여행자가 원하는 날짜와 조건에 맞추어 가이드와 여행계약을 체결하고 가이드서비스를 제공받는 것을 중개하는 업무를 수행합니다.
    
제4조 (가이드의 독립당사자 지위)
  1. 당사는 여행자와 가이드 사이에 투어를 중개할 뿐이며, 가이드는 당사와 고용 관계에 있지 않음을 확인합니다. 또한, 당사는 가이드에 대하여 사용자로서의 책임을 일체 부담하지 않는다는 점을 명확히 합니다.
 2. 가이드는 가이드약관의 이행에 필요한 관련 법령상의 허가, 등록, 신고 또는 보험, 공제, 예치 등의 의무사항을 모두 이행하여야 하며, 당사는 가이드가 관련 법령 위배로 인하여 여행자 또는 제3자에 대하여 손해배상 등 책임을 지더라도 당사는 이에 관하여 아무런 책임이 없습니다.
    
제5조 (여행계약의 구성)
 여행계약은 여행확인증과 여행자약관 및 가이드약관에 나타난 사항을 그 계약내용으로 합니다.
    
제6조 (당사자 및 당사의 기본 의무)
 1. 가이드는 사전에 여행자와 약정한 내용에 따라 투어를 성실하게 진행하여야 하며, 그밖에 여행자에게 안전하고 만족스러운 가이드서비스를 제공하기 위하여 여행계약상 의무를 성실하게 이행하여야 합니다.
 2. 여행자는 여행계약에 따른 의무를 성실하게 이행하며, 안전하고 즐거운 여행을 위하여 여행자간 화합도모 및 가이드의 여행질서 유지에 적극 협조합니다.
 3. 당사는 여행계약 체결의 중개 행위 등에 있어 맡은 바 임무를 충실히 수행합니다.
    
제7조 (가이드가 제공하는 용역의 내용)
가이드는 가이드약관에 따라 여행자에 대하여 다음 각 호의 용역을 제공합니다.
 (1) 여행자의 의사 및 제반 사정을 고려한 투어 일정의 계획 및 조정
 (2) 투어를 위하여 여행자와 만나기로 약속한 장소(이하 “미팅포인트”)에서 일정에 정한 각 여행지로 여행자를 인솔
 (3) 각 여행지에 대한 구체적인 안내 및 설명 제공
 (4) 당일 최종 여행지로부터 여행자의 숙소 등 일정에 정한 해산 지점으로 여행자를 인솔
 (5) 여행지에서 여행자의 원활한 의사소통 협조
 (6) 사고 등 문제 발생시의 여행자 보호 조치
 (7) 기타 투어 관련 제반 업무

                </textarea>
              </div>
              <br />
              <br />
              <div class="row">
                <div class="subject-text">개인정보 및 고유식별 정보 수집 및 이용동의</div>
                <textarea
                  class="form-control"
                  id="floatingTextarea2"
                  style="height: 300px; padding: 10px"
                  rows="10"
                  readonly
                >
❏ 개인정보의 수집·이용 목적

 ❍ 귀하의 개인정보는 가이드 등록을 위한 목적으로 수집·이용됩니다.
                                     
❏ 수집·이용할 개인정보의 항목
                                     
 ❍ 수집·이용되는 귀하의 개인정보는 다음과 같습니다.
                                     
 필수정보
                                     
  - 사진 및 인적사항(성명, 생년월일, 휴대전화번호, 주소, 이메일주소, 여권 등)
                                     
 선택정보
                                     
  - 자기소개 및 우대요건과 관련된 자격증 등

❏ 개인정보의 보유·이용기간
                                     
 ❍ 개인정보는 원칙적으로 개인정보의 수집 및 이용목적이 종료되면 파기합니다.
                                    </textarea
                >
              </div>
              <div class="booking_tour_form_submit pt-4" style="float:right; text-align: right;">
                <div class="form-check write_spical_check">
                  <label class="form-check-label" for="flexCheckDefaultf1">
                    <input
                    class="form-check-input"
                    type="checkbox"
                    value=""
                    v-model="check"
                    id="flexCheckDefaultf1"
                  />
                    위 약관을 읽고 이에 동의합니다. 
                  </label>
                </div>
                <button class="btn btn_theme btn_md">신청하기</button>
                <!-- <router-link to="/booking-confirmation" class="btn btn_theme btn_md"
                  >Sign up</router-link
                > -->
              </div>
              <div style="clear:both;"></div>
            </form>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import axios from "@/api/http";
import { mapState } from "vuex";
import router from "@/router";
import { ref, onMounted, reactive } from "vue";
import { useStore } from 'vuex';
import { useRouter } from "vue-router";

const accountStore = "accountStore";

export default {
  name: "VendorForm",
  data() {
    return {
      file1: null,
      file2: null,
      file3: null,
      check: false,
    };
  },
  created() {},
  computed: {
    ...mapState(accountStore, ["accessToken"]),
  },

  methods: {
    register() {
      if (this.check == false) alert("약관에 동의하세요");
      else {
        //console.log(this.accessToken, this.file1, this.file2, this.file3);

        const formData = new FormData();
        formData.append("idFile", this.file1);
        formData.append("certificateResidence", this.file2);
        formData.append("certificate", this.file3);

        axios
          .post(
            "/users/guide",
            {
              idFile: formData.get("idFile"),
              certificateResidence: formData.get("certificateResidence"),
              certificate: formData.get("certificate"),
            },
            {
              headers: {
                Authorization: "Bearer " + this.accessToken,
                "Content-Type": "multipart/form-data",
              },
            }
          )
          .then((res) => {
            console.log(res.data), router.push({ name: "home" });
          });
      }
    },
    selectFile1() {
      this.file1 = this.$refs["img1"].files[0];
    },
    selectFile2() {
      this.file2 = this.$refs["img2"].files[0];
    },
    selectFile3() {
      this.file3 = this.$refs["img3"].files[0];
    },
  },

  setup() {
    const store = useStore()
    const router = useRouter();
    onMounted(async () => {
      const isLogin = store.getters['accountStore/getLogin']
      if (!isLogin) {
        alert("로그인 먼저 해주세요.")
        router.push({ name: "login" });
      }
    })
  }
};
</script>

<style scoped>
.subject-text {
  color: var(--main-color);
  font-weight: bold;
}

.main-text {
  font-size: 40px;
  font-weight: bold;
  color: var(--main-color);
}
</style>
