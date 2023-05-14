<template>

    <!-- Common Banner Area -->
    <!-- <DashboardBanner /> -->

    <!-- Dashboard Area -->
    <DashboardArea />

    <!-- Cta Area -->



</template>
<script>
import DashboardBanner from '@/components/dashboard/DashboardBanner.vue'
import DashboardArea from '@/components/dashboard/DashboardArea.vue'


export default {
    name: "DashboardView",
    components: {
        DashboardBanner, DashboardArea
    }
};
</script>