<template>
    <div class = "container">
        <div class="row">
            <!-- Side Bar -->
            <sideBar />

            <innerContent />

        </div>
    </div>
</template>
<script>
import sideBar from '@/components/admin/sideBar.vue';
import innerContent from '@/components/admin/InnerContent.vue';

export default {
    name: "AdminView",
    components: {
        sideBar, innerContent
    }
};
</script>
