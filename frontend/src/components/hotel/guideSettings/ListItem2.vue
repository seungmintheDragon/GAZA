<template>
    <span class="language badge bg-primary">{{ thema.themaName }}</span><span class="lang-delete" @click="deleteThema(thema.themaId, index)">x</span>
</template>

<script>
import { getCurrentInstance } from 'vue';
export default {
    props: {
        index: Number,
        thema: Object,
    },
    setup() {
        const {emit} = getCurrentInstance();

        const deleteThema = function(ThemaId, index){
            console.log(ThemaId+" "+index);
            emit("deleteThema", ThemaId, index);
        }

        return{
            deleteThema
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
