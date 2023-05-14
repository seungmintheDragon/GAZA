<template>

    <section id="common_author_area" class="section_padding" >
        <div style="padding-top: 10px;"></div>
        <div class="container">
            <div class="row">
                <div class="col-lg-8 offset-lg-2">
                    <div class="common_author_boxed">
                        <div class="common_author_heading">
                            <h2>로그인</h2>
                        </div>
                        <div class="common_author_form">
                            <form @submit.prevent="Loginplease" id="main_author_form" enctype="multipart/form-data">
                                <div class="form-group">
                                    <input type="text" class="form-control" placeholder="아이디를 입력해 주세요." v-model="state.form.id" required/>
                                </div>
                                <div class="form-group">
                                    <input type="password" class="form-control" placeholder="비밀번호를 입력해 주세요." v-model="state.form.password" required/>
                                   
                                </div>
                                <div class="common_form_submit">
                                    <button class="btn btn_theme btn_md">로그인</button>
                                </div>
                                <div class="have_acount_area">
                                    <p>아이디가 없으십니까? <router-link to="/register">회원 가입</router-link></p>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>
    
<script>
import { reactive, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import router from "@/router";

export default {
    name: "CommonAuthorFour",

    setup(props, { emit }) {
        const store = useStore()

        const state = reactive({
            form: {
                id:'',
                password:'',
            }
        })

        const Loginplease = async function () {
                await store.dispatch('accountStore/loginAction', { id: state.form.id, password: state.form.password })
            }      
            
        onMounted(() => {
            if (store.getters["accountStore/getIsLogin"]) {
                router.push({name: "home"})
            }
        })
        
        return {state, Loginplease}
    }
    


};
</script>
