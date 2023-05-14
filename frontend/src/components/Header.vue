<template>
    <div>
        <header class="main_header_arae">
            <!-- Navbar Bar -->
            <div class="navbar-area">
                <div class="main-responsive-nav">
                    <div class="container">
                        <div class="main-responsive-menu mean-container">
                            <nav class="navbar">
                                <div class="container">
                                    <div class="logo">
                                        <router-link to="/">
                                            <img src="../assets/img/white_logo.png" alt="logo">
                                        </router-link>
                                    </div>
                                    <button class="navbar-toggler collapsed" type="button" data-bs-toggle="collapse"
                                        data-bs-target="#navbar-content">
                                        <div class="hamburger-toggle">
                                            <div class="hamburger">
                                                <span></span>
                                                <span></span>
                                                <span></span>
                                            </div>
                                        </div>
                                    </button>
                                    <div class="collapse navbar-collapse mean-nav" id="navbar-content">
                                        <ul class="navbar-nav mr-auto mb-2 mb-lg-0">
                                            <li class="홈으로">
                                                <router-link to="/" class="dropdown-item">홈으로</router-link>
                                            </li>
                                            <li class="nav-item" v-if="!islogin">
                                                <router-link to="/login" class="dropdown-item">로그인</router-link>
                                            </li>
                                            <li class="nav-item" v-if="islogin">
                                                <router-link to="/" @click="clickLogout" class="dropdown-item">로그아웃</router-link>
                                            </li>
                                            <li class="nav-item" v-if="!islogin">
                                                <router-link to="/register" class="dropdown-item">회원가입</router-link>
                                            </li>
                                            <li class="nav-item" v-if="islogin">
                                                <router-link to="/my-profile" class="dropdown-item">내 정보</router-link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
                <div class="main-navbar">
                    <div class="container">
                        <nav class="navbar navbar-expand-md navbar-light">
                            <router-link class="navbar-brand" to="/">
                                <img src="../assets/img/logo.png" alt="logo">
                            </router-link>

                            <div class="collapse navbar-collapse mean-menu" id="navbarSupportedContent">
                                <div class="others-options d-flex align-items-center">
                                    <div class="option-item">
                                        <router-link to="/room-details" class="btn btn_navber" v-if="isGuide == 'US1'">가이드 마이페이지</router-link>
                                        <button to="/" class="btn btn_navber" v-else-if="isGuide == 'US3'" disabled>가이드 심사중</button>
                                        <router-link to="/become-vendor" class="btn btn_navber" v-else >가이드로 가입하기</router-link>
                                    </div>
                                    <div class="option-item" v-if="isGuide == 'US4'">
                                        <router-link to="/admin" class="btn btn_navber" v-if="isGuide == 'US4'">어드민 페이지</router-link>
                                    </div>
                                    <div class="option-item" v-if="!islogin">
                                        <router-link to="/login" class="btn btn_navber">로그인</router-link>
                                    </div>
                                    <div class="option-item" v-if="islogin">
                                        <router-link to="/" @click="clickLogout" class="btn btn_navber">로그아웃</router-link>
                                    </div>
                                    <div class="option-item" v-if="!islogin">
                                        <router-link to="/register" class="btn btn_navber">회원가입</router-link>
                                    </div>
                                    <div class="option-item" v-if="islogin">
                                        <router-link to="/my-profile" class="btn btn_navber">내 정보</router-link>
                                    </div>
                                </div>
                            </div>
                        </nav>
                    </div>
                </div>
            </div>
        </header>
    </div>
</template>
<script>
import { reactive, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import router from "@/router";
import { STATEMENT_OR_BLOCK_KEYS } from '@babel/types';


export default {
    name: 'Header',
    data() {
        return {
            language: 'English',
            currency: 'USD',
            isSticky: false,
        }
    },

    mounted() {
        window.addEventListener('scroll', () => {
            let scroll = window.scrollY
            if (scroll >= 200) {
                this.isSticky = true
            } else {
                this.isSticky = false
            }
        })

        document.addEventListener('click', function (e) {
            // Hamburger menu
            if (e.target.classList.contains('hamburger-toggle')) {
                e.target.children[0].classList.toggle('active');
            }
        })
    },
    setup() {
        const store = useStore()

        const islogin = computed(() => {
           return store.getters["accountStore/getIsLogin"]
        })

        const isGuide = computed(() => {
            return store.getters["accountStore/getIsGuide"]
        })
        
        const clickLogout = () => {
            store.commit('accountStore/logOutData');
            router.go({name:"home"});
        }

        onMounted(() => {
            console.log(isGuide)
        })

        return { islogin, clickLogout, isGuide }
        
    },
    
}
</script>

