<template>
<div class="col-lg-4 col-md-6 col-sm-12 col-12">
    <div class="language-list-box">
        <list-item v-for="(lang,index) in language_list" :key="index" :lang="lang" @deleteLanguage="deleteLang"></list-item>
    </div>
    <div class="news_item_boxed">
    <form id="profile_form_area" @submit.prevent="langRegister">
        <select class="form-select" aria-label="Default select example" v-model="language" style="margin-bottom: 30px;">
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
import { guideLangRegister, getGuideLang, deleteLanguage , codeList } from "@/../common/api/commonAPI.js";
import ListItem from "@/components/hotel/guideSettings/ListItem";

export default {
    components:{
        ListItem,
    },
    setup() {
        const language_list = ref();
        const store = useStore();
        const language = ref();

        const codeNameList = ref();

        const loginId = store.getters["accountStore/getUserId"];

        //가이드 언어 조회
        const getLangList = async (loginId) => {
            const response = await getGuideLang(loginId); //call axios
            language_list.value = response.data;
            console.log("getLangList");
            console.log(language_list.value);
        };

        
        //언어 코드 목록 조회
        const getCodeList = async(lang) =>{
            const response = await codeList(lang);
            codeNameList.value = response.data;
            console.log(codeNameList.value);
        }


        onMounted(() => {
            getLangList(loginId);

            const param = "언어";
            getCodeList(param);

        });

        //가이드 사용 가능한 언어 등록
        const langRegister = async function () {
            const request = language.value;
            const payload = {
            languageName: request,
            loginId: loginId,
            };

            await guideLangRegister(payload); //call axios
            await getLangList(loginId);
            console.log("langRegister");
            console.log(language_list.value);
        };

        // 언어 삭제
        const deleteLang = async function(langId, index) {
            console.log(langId+" "+loginId);
            language_list.value.splice(index, 1);
            await deleteLanguage(langId, loginId); //call axios
        };
        

        return{
            language,
            langRegister,
            store,
            language_list,
            deleteLang,
            codeNameList,
            getCodeList
        };
    },
}
</script>

<style scoped>

.language-list-box {
    margin-top: 20px;
}

</style>