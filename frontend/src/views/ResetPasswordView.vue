<template>

    <!-- Common Banner Area -->
    <ResetPasswordBanner />

    <!--  Common Author Area -->
    <CommonAuthorTwo />



</template>
<script>
import ResetPasswordBanner from '@/components/user/ResetPasswordBanner.vue'
import CommonAuthorTwo from '@/components/user/CommonAuthorTwo.vue'


export default {
    name: "ResetPasswordView",
    components: {
        ResetPasswordBanner, CommonAuthorTwo
    }
};
</script>
