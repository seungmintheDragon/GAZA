<template>

    <!-- Common Banner Area -->
    <TourBanner />

    <!-- Form Area -->
    <TourForm />

    <!-- Tour Search Areas -->
    <TourSearch :search="searchItem" />


</template>
<script>
import TourBanner from '@/components/tour/TourBanner.vue'
import TourForm from '@/components/tour/TourForm.vue'
import TourSearch from '@/components/tour/TourSearch.vue'

import { reactive, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from "vue-router";

export default {
    name: "TourSearchView",
    components: {
        TourBanner, TourForm, TourSearch
    },

    setup(props, { emit }) {
        const searchItem = ref()

        const route = useRoute()



        onMounted(() => {
            searchItem.value = route.params.searchItem

        })

        return { searchItem, }
    }
};
</script>
