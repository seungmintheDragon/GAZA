<template>


    <!-- About Us -->
    <AboutInfo />

    <!-- About Banner -->
    <AboutOffer />

    <!-- About Service Area -->
    <AboutService />

    <!-- consultation Area -->
    <Consultation />

    <!-- Counter Area -->
    <Counter />

    <!-- Customer Area -->
    <Customer />

    <!-- Cta Area -->



</template>
<script>

import AboutInfo from '@/components/about/AboutInfo.vue'
import AboutOffer from '@/components/about/AboutOffer.vue'
import AboutService from '@/components/about/AboutService.vue'
import Consultation from '@/components/about/Consultation.vue'
import Counter from '@/components/about/Counter.vue'
import Customer from '@/components/about/Customer.vue'


export default {
    name: "AboutView",
    components: {
        AboutInfo, AboutOffer, AboutService,  Consultation, Counter, Customer
    }
};
</script>