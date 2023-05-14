<template>

  <Header v-if="$route.name !== 'admin' && $route.name !== 'mapdetail'" />
  <router-view />

  <Footer v-if="$route.name !== 'admin' && $route.name !== 'mapdetail'" />

</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { reactive, computed, ref, onMounted, watch } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'App',
  components: {
    Header, Footer
  },
  setup() {
    const store = useStore()
    onMounted(() => {
      store.dispatch("accountStore/confirmAction")
    }) 
  }
}
</script>
