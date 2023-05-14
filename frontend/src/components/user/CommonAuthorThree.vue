<template>
    <section id="common_author_area" class="section_padding">
        <div class="container">
            <div class="row">
                <div class="col-lg-8 offset-lg-2">
                    <div class="common_author_boxed">
                        <div class="common_author_heading">
                            <h3>회원 가입</h3>
                        </div>
                        <div class="common_author_form">
                            <form @submit.prevent="Signin" id="main_author_form" class="row">
                                <div class="form-group">
                                    <span style="position: relative;">
                                        <input type="text" :class="{ 'formerror': idc }" class="form-control"  placeholder="아이디를 입력해주세요." minlength="4" v-model="state.form.id" @keyup="upid" required/>
                                        <button type="button" class="btn btn_navber" @click="duplicated" style="position: absolute; top: 13%; right: 20px;">중복확인</button>
                                    </span>
                                </div>
                                <div class="form-group">
                                    <label for="formFile" class="form-label">프로필 사진 업로드</label>
                                    <input class="form-control" type="file" id="formFile" accept="image/*" @change="upload" ref="pictureData">
                                    
                                </div>
                                <div class="form-group col-6">
                                    <input type="text" class="form-control" placeholder="이메일을 입력해 주세요." v-model="state.form.email" required/>
                                </div>
                                <div class="form-group col-1 position-relative">
                                    <h3 class="position-absolute top-50 start-50 translate-middle">@</h3>
                                </div>
                                <div class="form-group col-5">
                                    <input type="text" class="form-control" v-model="state.form.email_domain" :class="{ 'formerror': emailc }" @keyup="upemail" required/>
                                    <p v-if="emailc">이메일이 잘못되었습니다.</p>
                                </div>
                                <div class="form-group">
                                    <select class="form-select form-control" id="validationCustom04" required v-model="state.form.gender">
                                        <option selected disabled value="">성별을 골라주세요</option>
                                        <option value="man" >남자</option>
                                        <option value="woman">여자</option>
                                    </select>
                                </div>
                                <div class="form-group col-3">
                                    <input type="text" class="form-control" placeholder="전화번호" disabled readonly/>
                                </div>
                                <div class="form-group col-3">
                                    <input required type="text" class="form-control" maxlength='4' minlength="1" v-model="state.form.phonefront" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\.-/g, '$1');"/>
                                </div>
                                <div class="form-group col-3">
                                    <input required type="text" class="form-control" maxlength='4' minlength="1" v-model="state.form.phonemiddle" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\.-/g, '$1');"/>
                                </div>
                                <div class="form-group col-3">
                                    <input required type="text" class="form-control" maxlength='4' minlength="1" v-model="state.form.phoneend"  oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\.-/g, '$1');"/>
                                </div>
                                <div class="form-group col-3">
                                    <input type="text" class="form-control" placeholder="생년월일" disabled readonly/>
                                </div>
                                <div class="form-group col-3">
                                    <input required type="text" class="form-control" v-model="state.form.birthdayf" maxlength='4' minlength="4" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\.-/g, '$1');"/>
                                </div>
                                <div class="form-group col-3">
                                    <select required name="month" id="month" class="form-control custom-select" v-model="state.form.birthdaym">
                                        <option value="01">1월</option>
                                        <option value="02">2월</option>
                                        <option value="03">3월</option>
                                        <option value="04">4월</option>
                                        <option value="05">5월</option>
                                        <option value="06">6월</option>
                                        <option value="07">7월</option>
                                        <option value="08">8월</option>
                                        <option value="09">9월</option>
                                        <option value="10">10월</option>
                                        <option value="11">11월</option>
                                        <option value="12">12월</option>
                                    </select>
                                </div>
                                <div class="form-group col-3">
                                    <select required name="day" id="day" class="form-control custom-select" v-model="state.form.birthdaye">
                                        <option value="01" selected="selected">1일</option>
                                        <option value="02">2일</option>
                                        <option value="03">3일</option>
                                        <option value="04">4일</option>
                                        <option value="05">5일</option>
                                        <option value="06">6일</option>
                                        <option value="07">7일</option>
                                        <option value="08">8일</option>
                                        <option value="09">9일</option>
                                        <option value="10">10일</option>
                                        <option value="11">11일</option>
                                        <option value="12">12일</option>
                                        <option value="13">13일</option>
                                        <option value="14">14일</option>
                                        <option value="15">15일</option>
                                        <option value="16">16일</option>
                                        <option value="17">17일</option>
                                        <option value="18">18일</option>
                                        <option value="19">19일</option>
                                        <option value="20">20일</option>
                                        <option value="21">21일</option>
                                        <option value="22">22일</option>
                                        <option value="23">23일</option>
                                        <option value="24">24일</option>
                                        <option value="25">25일</option>
                                        <option value="26">26일</option>
                                        <option value="27">27일</option>
                                        <option value="28">28일</option>
                                        <option value="29">29일</option>
                                        <option value="30">30일</option>
                                        <option value="31">31일</option>
                                    </select>                                    
                                </div>
                                <div class="form-group">
                                    <input required type="text" class="form-control" placeholder="이름" v-model="state.form.name" minlength="1" />
                                </div>
                                <div class="form-group">
                                    <input required type="password" class="form-control" placeholder="비밀번호" :class="{ 'formerror': passwordc }" @keyup="uppassword" v-model="state.form.password" minlength="6"/>
                                </div>
                                <div class="form-group">
                                    <input required type="password" class="form-control" :class="{ 'formerror': passwordc }" @keyup="uppassword" placeholder="비밀번호 확인" v-model="passwordcheck" minlength="6"/>
                                    <p v-if="passwordc">비밀번호가 일치하지 않습니다.</p>
                                </div>
                                <div class="common_form_submit">
                                    <button class="btn btn_theme btn_md" >회원가입</button>
                                </div>
                                
                                <div class="have_acount_area other_author_option">

                                    <p>계정이 있으십니까? <router-link to="/login">로그인</router-link></p>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

</template>
<style>
.formerror{
    border: 1px solid red !important;
}
</style>
    
<script>
import { reactive, ref } from 'vue'
import { requestSignin, checkDuplicated } from "../../../common/api/commonAPI"
import router from "@/router";

export default {
    name: "CommonAuthorThree",

    setup() {
        const passwordcheck = ref('')
        const pictureData = ref(null)
        const idc = ref(false)
        const passwordc =ref(false)
        const emailc = ref(false)
        
        const state = reactive({
            form: {
                id: '',
                password: '',
                name:'',
                gender:'',
                birthdayf:'',
                birthdaym:'',
                birthdaye:'',
                phonefront:'',
                phonemiddle:'',
                phoneend:'',
                email:'',
                email_domain:'',
            },
            duplicated : false,
        })

        const idCheck = function() {
            state.duplicated = false;
        }

        const duplicated = async function() {
            if(state.form.id === ''){
                idc.value = true
                alert("아이디를 확인해주세요");
            } else {
                const res = await checkDuplicated(state.form.id);

                const user = res.data.result;

                console.log(user);

                if(user !== null) {
                    idc.value = true
                    alert("중복된 아이디입니다.");
                } else {
                    idc.value = false
                    alert("사용 가능한 아이디입니다.");
                }
            }
        }

        const Signin = async function () {
            console.log('submit sign');
            if (!state.form.email_domain.includes('.')) {
                if (state.form.email_domain) {
                    emailc.value = true
                }
            } else {
                emailc.value = false
            }

            if (state.form.password !== passwordcheck.value) {
                passwordc.value = true
            } else {
                passwordc.value = false
            }

            if (!passwordc.value && !emailc.value && !idc.value) {
                try {
                    const formData = new FormData()
                    
                    // formData.append("piture", pictureData.value.files[0])
                    // formData.append("piture", null)
                    // formData.append("user", state.form)

                    let userpi = {
                        id: state.form.id,
                        password: state.form.password,
                        name:state.form.name,
                        gender:state.form.gender,
                        birthday:`${state.form.birthdayf}-${state.form.birthdaym}-${state.form.birthdaye}T14:35:34.008Z`,
                        phone_number:`${state.form.phonefront}-${state.form.phonemiddle}-${state.form.phoneend}`,
                        email:state.form.email,
                        email_domain:state.form.email_domain,
                    }
                    console.log(JSON.stringify(userpi))
                    let payload = {
                        picture: pictureData.value.files[0],
                        user: JSON.stringify(userpi)
                    }

                    const response = await requestSignin(payload)
                    console.log(response);
                    alert("회원 가입 완료")
                    router.push({name: "login"})
                } catch (error) {
                    console.log(error);
                }
            }

            
        }

        const upemail = function ()  {
            emailc.value = false
        }

        const uppassword = function () {
            passwordc.value = false
        }

        const upload = async() => {
           // debugger;
            console.log("selected file", pictureData.value.files)
            // state.form.picture = pictureData.value.files
            // console.log(state.form.picture);
            //Upload to server
        }

        const upid = function () {
            idc.value = false
        }

        return { state, Signin, idc, passwordc, emailc, upemail, uppassword, pictureData, idCheck, upload, passwordcheck, duplicated, upid}
     },
    
};
</script>
