<template>

    <!-- Common Banner Area -->
    <TourGuideBanner />

    <!-- Tour Guides Area -->
    <TourGuides />




</template>
<script>

import TourGuideBanner from '@/components/tour/TourGuideBanner.vue'
import TourGuides from '@/components/tour/TourGuides.vue'

export default {
    name: "TourGuidesView",
    components: {
        TourGuideBanner, TourGuides
    }
};
</script>