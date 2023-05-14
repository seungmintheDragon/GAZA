<template>

    <!-- Common Banner Area -->
    <TourBanner />

    <!-- Form Area -->
    <TourFormCopy />

    <!-- Tour Search Areas -->
    <TourSearchCopy :search="searchItem" />


</template>
<script>
import TourFormCopy from '@/components/tour/TourFormCopy.vue'
import TourBanner from '@/components/tour/TourBanner.vue'
import TourSearchCopy from '@/components/tour/TourSearchCopy.vue'

import { reactive, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from "vue-router";

export default {
    name: "TourSearchViewCopy",
    components: {
        TourBanner, TourFormCopy, TourSearchCopy
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
