<template>
<div class="col-lg-4 col-md-6 col-sm-12 col-12">
    <div class="thema-list-box">
        <list-item2 v-for="(thema,index) in thema_list" :key="index" :index="index" :thema="thema" @deleteThema="delThema"></list-item2>
    </div>
    <div class="news_item_boxed">
    <form id="profile_form_area" @submit.prevent="themaRegister">
        <select class="form-select" aria-label="Default select example" v-model="thema"  style="margin-bottom: 30px;">
            <option v-for="(idx) in codeNameList" :value="idx.description">{{idx.description}}</option>
        </select>
        <button class="btn btn_theme btn_sm">submit</button>
    </form>
    </div>
</div>
</template>

<script>
import { useStore } from "vuex";
import { onMounted, ref } from "vue";
import { guideThemaRegister, getGuideThema, deleteThema , codeList} from "@/../common/api/commonAPI.js";
import ListItem2 from "@/components/hotel/guideSettings/ListItem2";

export default {
    components:{
        ListItem2,
    },
    setup() {
        const thema_list = ref();
        const store = useStore();
        const thema = ref();

        const loginId = store.getters["accountStore/getUserId"];

        const codeNameList = ref();

        //가이드 테마 조회
        const getThemaList = async (loginId) => {
            const res = await getGuideThema(loginId); //call axios
            thema_list.value = res.data;
        };

        //테마 코드 조회
        const getCodeList = async(param)=>{
            const res = await codeList(param);
            codeNameList.value = res.data;
        }

        onMounted(() => {
            getThemaList(loginId);

            const param = "테마";
            getCodeList(param);
        });

        //테마 등록
        const themaRegister = async function () {
            await guideThemaRegister(loginId, thema.value); //call axios
            await getThemaList(loginId);
        };

        // 테마 삭제
        const delThema = async function(themaId, index) {
            thema_list.value.splice(index, 1);
            console.log(loginId+" "+themaId);
            await deleteThema(loginId, themaId); //call axios
        };
        

        return{
            thema,
            themaRegister,
            store,
            thema_list,
            delThema,
            codeNameList,
            getCodeList
        };
    },
}
</script>

<style scoped>

.thema-list-box {
    margin-top: 20px;
}

</style>