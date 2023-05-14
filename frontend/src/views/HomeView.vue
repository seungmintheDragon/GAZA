<template>

  <Banner />

  <Form />
  
  <UserBook/>

  <PromotionalTours />

  <PromotionalToursRecommand />


</template>

<script>
// @ is an alias to /src
import Banner from '@/components/home/Banner.vue'
import Form from '@/components/home/Form.vue'
import UserBook from '@/components/home/UserBook.vue'
import PromotionalTours from '@/components/home/PromotionalTours.vue'
import PromotionalToursRecommand from '@/components/home/PromotionalToursRecommand.vue'

import { reactive, computed, ref, onMounted, watch } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'HomeView',
  components: {
    Banner, Form, UserBook,PromotionalTours,  PromotionalToursRecommand
  },

  setup() {
    const store = useStore()
    onMounted(() => {
      store.dispatch("accountStore/confirmAction")
    }) 
  }
}
</script>