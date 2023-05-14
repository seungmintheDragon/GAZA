<template>

    <!-- Common Banner Area -->
    <ErrorBanner />

    <!--Error Areas -->
    <ErrorArea />



</template>
<script>
import ErrorBanner from '@/components/pages/ErrorBanner.vue'
import ErrorArea from '@/components/pages/ErrorArea.vue'


export default {
    name: "ErrorView",
    components: {
        ErrorBanner, ErrorArea
    }
};
</script>