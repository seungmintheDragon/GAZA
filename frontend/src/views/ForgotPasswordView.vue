<template>

    <!-- Common Banner Area -->
    <ForgotBanner />

    <!--  Common Author Area -->
    <CommonAuthorFive />



</template>
<script>
import ForgotBanner from '@/components/user/ForgotBanner.vue'
import CommonAuthorFive from '@/components/user/CommonAuthorFive.vue'


export default {
    name: "ForgotPasswordView",
    components: {
        ForgotBanner, CommonAuthorFive
    }
};
</script>