<template>
    <div>
        <!-- Common Banner Area -->

        <!-- <LoginBanner /> -->
    
        <!--  Common Author Area -->
        <CommonAuthorFour/>
    
        <!-- Cta Area -->
    </div>



</template>
<script>
import LoginBanner from '@/components/user/LoginBanner.vue'
import CommonAuthorFour from '@/components/user/CommonAuthorFour.vue'


export default {
    name: "LoginView",
    components: {
        LoginBanner, CommonAuthorFour,
    }
};
</script>