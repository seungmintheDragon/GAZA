<template>
    <div class="container" id="app" v-cloak>
        <div class="row">
            <div style="height: 100px"></div>
            <div class="col-md-12">
                <h2>💬 방</h2>
            </div>
        </div>
        <div class="input-group">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="createConsulting">
                    개설
                </button>
            </div>
        </div>
        <ul class="list-group">
            <li
                class="list-group-item list-group-item-action"
                v-for="item in maprooms"
                v-bind:key="item.roomId"
                @click="enterConsulting(item.roomId)"
            >
                {{ item.name }}
            </li>
        </ul>
    </div>
</template>

<script>
import axios from "axios";

axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = "https://i8c207.p.ssafy.io/api";
// const APPLICATION_SERVER_URL = "http://localhost:8080";

export default {
    data() {
        return {
            // 아래는 현재 있다고 가정 (예약 내역)
            userName: "SSAFY 8기 아이들",
            reservationId: 3,
            guidePK: 1,
            guideId: "SSAFY2",
            // ---------------------------------
            // OpenVidu 객체들
            OpenVidu: {
                OV: undefined,
                session: undefined,
                mainStreamManager: undefined,
                publisher: undefined,
                subscribers: [],
            },
            // Join form
            mySessionId: "SessionA",
            maprooms: [],
        };
    },
    created() {
        this.findAllRoom();
    },
    methods: {
        findAllRoom() {
            axios.get(APPLICATION_SERVER_URL + "/map/rooms").then((response) => {
                this.maprooms = response.data;
                console.log("findAllRoom");
                console.log(this.maprooms);
            });
        },
        async createMapRoom() {
            var base = this;
            console.log("createMapRoom");
            await axios
                .post(APPLICATION_SERVER_URL + "/map/room", this.reservationId) // 원래는 room_name이지만 방제목은 안 쓸 거니까.
                .then((data) => {
                    console.log("[" + data.data.roomId + "] 개설");
                    base.mySessionId = data.data.roomId;
                })
                .catch(() => {
                    alert("지도방 개설에 실패하였습니다.");
                });
            // await axios
            // .post(APPLICATION_SERVER_URL + "/books/consulting/create/"+ this.reservationId, base.mySessionId)
            // .then((data) => {
            //   console.log("예약 테이블에 세션 아이디 추가. " + data);
            // })
            // .catch(() => {
            //   alert("예약 데이터 업데이트에 실패하였습니다.");
            // });
        },
        enterConsulting(roomId) {
            this.$router.push({
                name: "mapdetail",
                params: {
                    roomId: roomId,
                    reservationId: this.reservationId,
                    guideId: this.guideId,
                    userName: this.userName,
                },
            });
        },
        async createConsulting() {
            await this.createMapRoom();
            const routeData = this.$router.resolve({
                name: "mapdetail",
                params: {
                    roomId: this.mySessionId,
                    reservationId: this.reservationId,
                    guideId: this.guideId,
                    userName: this.userName,
                },
            });
            window.open(routeData.href, "_blank");
        },
    },
};
</script>

<style>
[v-cloak] {
    display: none;
}
</style>
