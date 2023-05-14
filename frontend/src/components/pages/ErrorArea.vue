<template>
    <section id="error_main" class="section_padding">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 offset-lg-3 col-md-12 col-sm-12 col-12">
                    <div class="error_content text-center">
                        <img src="../../assets/img/common/error.png" alt="img">
                        <h2>Error 404 : page not found</h2>
                        <router-link to="/" class="btn btn_theme btn_md" @click="BackHome">Back to home</router-link>
                    </div>
                </div>
            </div>
        </div>
    </section>

</template>
<script>

export default {
    name: "ErrorArea",
    methods: {
        BackHome() {
            this.$router.go(-1)
        }
    }
};
</script>