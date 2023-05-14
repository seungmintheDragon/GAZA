<template>
    <div>
        <!-- <RegisterBanner /> -->
        <div style="padding-top: 10px;"></div>
        <CommonAuthorThree />
    </div>
</template>
<script>
import RegisterBanner from '@/components/user/RegisterBanner.vue'
import CommonAuthorThree from '@/components/user/CommonAuthorThree.vue'


export default {
    name: "RegisterView",
    components: {
        RegisterBanner, CommonAuthorThree,
    }
};
</script>
