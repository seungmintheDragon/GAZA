<template>
    <div v-for="group in groups">
        <li v-on:click="group.open = !group.open" class="dashboard_dropdown_button"><i class="fas fa-address-card"></i>
            My bookings
            <span> <i class="fas fa-angle-down"></i></span>
        </li>
        <div v-show="!group.open">
            <div class="booing_sidebar_dashboard">
                <ul>
                    <li>
                        <router-link to="/hotel-booking"><i class="fas fa-hotel"></i>Hotel
                            booking</router-link>
                    </li>
                    <li><router-link to="/flight-booking"><i class="fas fa-paper-plane"></i>Flight
                            booking</router-link></li>
                    <li>
                        <router-link to="/tour-booking">
                            <i class="fas fa-map"></i>Tour booking
                        </router-link>
                    </li>
                    <li><router-link to="/booking-history">
                            <i class="fas fa-history"></i>Booking history</router-link>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>

export default {
    name: "MyBookingOption",
    data() {
        return {
            groups: {
                "group 1": {
                    open: "false"
                }
            }
        }
    }
};
</script>