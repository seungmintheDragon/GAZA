<template>

    <!-- Common Banner Area -->
    <!-- <MyProfileBanner /> -->

    <!-- Dashboard Area -->
    <ProfileDashboard />


</template>
<script>

// import MyProfileBanner from '@/components/dashboard/MyProfileBanner.vue'
import ProfileDashboard from '@/components/dashboard/ProfileDashboard.vue'
import { onMounted } from 'vue';
import { useStore } from "vuex";
import router from "@/router";

export default {
    name: "MyProfileView",
    components: {
        ProfileDashboard
    } ,
    setup() {
        const store = useStore()
        onMounted(() => {
            const isLogin = store.getters["accountStore/getIsLogin"];
            if(isLogin == false){
                alert("로그인 후 이용하세요");
                router.push({name:"home"});
            }
        }) 
  }
   
};
</script>
