<template>

    <!-- Common Banner Area -->
    <!-- <NBanner /> -->

    <NotificationDashboard />


</template>



<script>
import NBanner from '@/components/dashboard/NBanner.vue'
import NotificationDashboard from '@/components/dashboard/NotificationDashboard.vue'

export default {
    name: "NotificationView",
    components: {
        NBanner, NotificationDashboard
    }
};
</script>