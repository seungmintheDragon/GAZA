<template>


  <section id="promotional_tours" class="section_padding_top">
    <div class="container">
      <!-- Section Heading -->
      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12 col-12">
          <div class="section_heading_center">
            <h2>인기 가이드</h2>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <div class="promotional_tour_slider owl-theme owl-carousel dot_style">
            <swiper :slides-per-view="4"  :space-between="20" :pagination="{ clickable: true }">
              <swiper-slide v-for="guide in state.form.swiperItems" :key="guide.name">
                <div class="theme_common_box_two img_hover">
                  <div class="theme_two_box_img">
                    <router-link :to="{
                      name: 'hotel-details',
                      params: { guideId: guide.guideId },
                  }"><img :src="baseURL+guide.picture" alt="img"></router-link>
                  </div>
                  <div class="theme_two_box_content">
                    <h4><router-link :to="{
                        name: 'hotel-details',
                        params: { guideId: guide.guideId  },
                      }"
                      >{{ guide.name }} <p><i class="fas fa-map-marker-alt"></i>{{ guide.country }}, {{ guide.city }}</p></router-link></h4>
                    <p><span class="review_rating">
                        <span v-for="lang in guide.language" :key="lang">#{{ lang }}</span>
                    </span> <span class="review_count">
                      <span v-for="theme in guide.thema" :key="theme" ><span v-if="theme != null">#{{ theme }}</span></span>
                    </span></p>
                    <h3>{{guide.price}} <span>Price starts from</span></h3>
                  </div>
                </div>
              </swiper-slide>
            </swiper>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
  
<script>
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/swiper-bundle.css";
import { reactive, onMounted } from 'vue';
import { popularGuide} from '../../../common/api/commonAPI';

export default {
  name: "PromotionalTours",

  components: {
    Swiper,
    SwiperSlide,
  },

  setup(props, { emit }) {

    const baseURL = "https://s3.ap-northeast-2.amazonaws.com/ssafy.common.gaza//gaza/guide/mypage/";

    const state = reactive({
      form: {
        swiperItems: null,
      },

    })

    const popularGuides = async function () {

      const response = await popularGuide()
      state.form.swiperItems = response.data

    }

    onMounted(() => {
      popularGuides()
    })

    return { popularGuides, state, baseURL}
  },
};
</script>