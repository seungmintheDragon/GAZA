<template>
    <div class="col-lg-2  col-md-6 col-sm-12 col-12">
        <div class="flight_Search_boxed dropdown_passenger_area">
            <p>Passenger, Class </p>
            <div class="dropdown">
                <button class="dropdown-toggle final-count" data-toggle="dropdown" type="button"
                    id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                    {{ number }} Guests
                </button>

                <div class="dropdown-menu dropdown_passenger_info" aria-labelledby="dropdownMenuButton1">
                    <div class="traveller-calulate-persons">
                        <div class="passengers">
                            <h6>Guests</h6>
                            <div class="passengers-types">
                                <div class="passengers-type">
                                    <div class="text"><span class="count pcount">{{
        pnumber
}}</span>
                                        <div class="type-label">
                                            <p>Adult</p>
                                            <span>12+
                                                yrs</span>
                                        </div>
                                    </div>
                                    <div class="button-set">
                                        <button type="button" class="btn-add" @click.stop="increment">
                                            <i class="fas fa-plus"></i>
                                        </button>
                                        <button type="button" class="btn-subtract" @click.stop="decrement">
                                            <i class="fas fa-minus"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="passengers-type">
                                    <div class="text"><span class="count ccount">{{
        cnumber
}}</span>
                                        <div class="type-label">
                                            <p class="fz14 mb-xs-0">
                                                Children
                                            </p><span>2
                                                - Less than 12
                                                yrs</span>
                                        </div>
                                    </div>
                                    <div class="button-set">
                                        <button type="button" class="btn-add-c" @click.stop="incrementc">
                                            <i class="fas fa-plus"></i>
                                        </button>
                                        <button type="button" class="btn-subtract-c" @click.stop="decrementc">
                                            <i class="fas fa-minus"></i>
                                        </button>
                                    </div>
                                </div>
                                <div class="passengers-type">
                                    <div class="text"><span class="count incount">{{
        inumber
}}</span>
                                        <div class="type-label">
                                            <p class="fz14 mb-xs-0">
                                                Infant
                                            </p><span>Less
                                                than 2
                                                yrs</span>
                                        </div>
                                    </div>
                                    <div class="button-set">
                                        <button type="button" class="btn-add-in" @click.stop="incrementi">
                                            <i class="fas fa-plus"></i>
                                        </button>
                                        <button type="button" class="btn-subtract-in" @click.stop="decrementi">
                                            <i class="fas fa-minus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <span>Business</span>
        </div>
    </div>
</template>
<script>

export default {
    name: "CountDropdownHotel",
    data() {
        return {
            number: 0,
            pnumber: 0,
            cnumber: 0,
            inumber: 0,
        };
    },
    methods: {


        increment() {
            this.number++;
            this.pnumber++;
        },

        decrement() {

            if (this.number === 0) return
            this.number -= 1

            if (this.pnumber === 0) return

            this.pnumber -= 1


        },


        incrementc() {
            this.number++;
            this.cnumber++;
        },

        decrementc() {

            if (this.number === 0) return
            this.number -= 1

            if (this.cnumber === 0) return

            this.cnumber -= 1
        },


        incrementi() {
            this.number++;
            this.inumber++;
        },

        decrementi() {

            if (this.number === 0) return
            this.number -= 1

            if (this.inumber === 0) return

            this.inumber -= 1
        },


    }
};
</script>