<template>
    <span class="language badge bg-primary">{{ lang.languageName }}</span><span class="lang-delete" @click="deleteLanguage(lang.language_id, index)">x</span>
</template>

<script>
import { getCurrentInstance } from 'vue';
export default {
    props: {
        index: Number,
        lang: Object,
    },
    setup() {
        const {emit} = getCurrentInstance();

        const deleteLanguage = function(langId, index){
            emit("deleteLanguage", langId, index);
        }

        return{
            deleteLanguage
        }
    },
}
</script>

<style scoped>

.language {
    font-size : 15px;
    margin-right: 3px;
}
.lang-delete {
    font-size : 15px;
    font-weight: bold;
    color : var(--main-color);
    margin-right: 10px;
    cursor : pointer;
}

</style>
