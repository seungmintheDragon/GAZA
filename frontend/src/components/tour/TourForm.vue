<template>
    <section id="theme_search_form">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="theme_search_form_area">
                        <div class="tab-content" id="myTabContent">
                            <div class="tab-pane fade show active" id="tours" role="tabpanel" aria-labelledby="tours-tab">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="tour_search_form">
                                            <form @submit.prevent="itemSearch">
                                                <div class="row">
                                                    <div class="col-lg-10 col-md-12 col-sm-12 col-12 flight_Search_boxed d-flex">
                                                        <img src="../../assets/img/icon/Icon.png" class="d-inline m-auto pe-2" alt="">
                                                        <input type="text" class="d-inline" placeholder="가고 싶은 도시나 나라를 검색해주세요." v-model="state.form.searchPlace">
                                                    </div>
                                                    <div class="top_form_search_button col-lg-2 col-md-12 col-sm-12 col-12">
                                                        <button class="btn btn_theme btn_md">Search</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
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
import { reactive, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from "vue-router";

export default {
    name: "TourForm",
    setup(props, { emit }) {
        const router = useRouter();
        
        const store = useStore()

        const state = reactive({
            form: {
                searchPlace: ''
            }
        })

        const itemSearch = function () {
            router.push({
                name: "tour-search-copy",
                params: {  
                    searchitem : state.form.searchPlace
                }
            })
        }

        return { state, itemSearch }
    }
    
};
</script>