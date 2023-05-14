<template>
  <section id="theme_search_form_tour">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="theme_search_form_area">
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="tour_search_form">
                                    <form action="!#">
                                        <div class="row">
                                            <div class="col-lg-6 col-md-12 col-sm-12 col-12">
                                                <div class="flight_Search_boxed">
                                                    <p>Destination</p>
                                                    <input type="text" placeholder="Where are you going?">
                                                    <span>Where are you going?</span>
                                                </div>
                                            </div>
                                            <div class="col-lg-4 col-md-6 col-sm-12 col-12">
                                                <div class="form_search_date">
                                                    <div class="flight_Search_boxed date_flex_area">
                                                        <div class="Journey_date">
                                                            <p>Check In date</p>
                                                            <input type="date" value="2022-05-03">
                                                            <span>Thursday</span>
                                                        </div>
                                                        <div class="Journey_date">
                                                            <p>Check Out date</p>
                                                            <input type="date" value="2022-05-05">
                                                            <span>Thursday</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                           <CountDropdownHotel />
                                            <div class="top_form_search_button">
                                                <button class="btn btn_theme btn_md" type="button">Search</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>
<script>

import CountDropdownHotel from '@/components/hotel/CountDropdownHotel.vue'

export default {
   name: "Form",
   components: {
    CountDropdownHotel
    },
};
</script>