<template>
    <div class="container" id="app" v-cloak>
        <div id="session-header">
            <input
                class="btn btn-outline-danger"
                type="button"
                id="buttonLeaveSession"
                @click="leaveSession"
                value="나가기"
                style="margin: 3px; float: right"
            />
        </div>
        <div style="clear: both"></div>
        <div>
            <div
                id="map"
                style="
                    width: 71%;
                    height: 400px;
                    position: relative;
                    overflow: hidden;
                    float: left;
                    margin-right: 10px;
                "
            ></div>
            <ul id="category">
                <li id="BK9" data-order="0" @click="sendPoint('FOCUS')">
                    <img src="./bell.png" />
                </li>
                <li
                    id="MT1"
                    data-order="1"
                    @click="saveRoute(clickIndex, clickRecommend, clickPopular)"
                >
                    <img src="./flag.png" />
                </li>
            </ul>
        </div>
        <div style="width: 27%; height: 400px; overflow: auto; border: 1px solid lightgray">
            <h3 class="route-list">여행 일정</h3>
            <div class="list-group">
                <list-item
                    v-for="(route, index) in travel_route"
                    :key="index"
                    :index="index"
                    :route="route"
                    @manageRoute="manageRoute"
                />
            </div>
        </div>
        <div style="clear:both:"></div>

        <div id="session" class="" style="width: 71%; height: 350px; margin: auto; float: left">
            <div id="video-container" style="width: 100%">
                <user-video
                    :stream-manager="OpenVidu.publisher"
                    @click="updateMainVideoStreamManager(OpenVidu.publisher)"
                />
                <user-video
                    v-for="sub in OpenVidu.subscribers"
                    :key="sub.stream.connection.connectionId"
                    :stream-manager="sub"
                    @click="updateMainVideoStreamManager(sub)"
                />
            </div>
        </div>
        <div class="chat">
            <div style="height: 85%; overflow: auto">
                <ul class="list-group">
                    <li
                        class="list-group-item"
                        style="text-align: left"
                        v-for="item in messages"
                        :key="item"
                    >
                        {{ item.writerName }} : {{ item.message }}
                    </li>
                </ul>
            </div>
            <div class="input-group-append">
                <input
                    type="text"
                    v-model="msg"
                    style="width: 80%; height: 35px"
                    v-on:keypress.enter="sendMessage"
                />
                <button class="btn btn-primary" type="button" @click="sendMessage">전송</button>
            </div>
        </div>
    </div>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import ListItem from "@/components/map/list/ListItem";
import axios from "axios";
import UserVideo from "@/openvidu/UserVideo";
import { OpenVidu } from "openvidu-browser";
import { mapState } from "vuex";
const accountStore = "accountStore";
axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = "https://i8c207.p.ssafy.io/api";
// const APPLICATION_SERVER_URL = "http://localhost:8080";

export default {
    components: {
        ListItem,
        UserVideo,
    },
    data() {
        return {
            // 라우터로부터 가져오는 데이터들.
            roomId: this.$route.params.roomId,
            userName: this.$route.params.userName,
            reservationId: this.$route.params.reservationId,
            guideId: this.$route.params.guideId,
            guidePk: this.$route.params.guidePk,
            // --------채팅 데이터-----------
            msg: "",
            messages: [],
            // -----------------------------
            guideCountry: "대한민국",
            guideCity: "광주",
            room: {},
            sender: "",
            clickLng: 33.450701,
            clickLat: 126.570667,
            clickIndex: -1,
            clickRecommend: false,
            clickPopular: false,
            map: Object,
            recvPoint: {
                recvLng: 0.0,
                recvLat: 0.0,
                type: "",
            },
            travel_route: [],
            recommend_location_list: [],
            markerImageSrc: require("./markers.png"),
            pointImageSrc: require("./point.png"),
            yourMarker: Object,
            OpenVidu: {
                OV: undefined,
                session: undefined,
                mainStreamManager: undefined,
                publisher: undefined,
                subscribers: [],
            },
            polyline: null,
            route_marker_list: [],
            recommended_marker_list: [],
            popular_marker_list: [],
            popular_list: [],
        };
    },
    computed: {
        ...mapState(accountStore, ["isGuide"]),
    },
    created() {
        console.log(this.isGuide);
        console.log(this.roomId);
        this.connect();
        this.getCity();
        this.getRecommend();
        this.joinSession();
    },
    mounted() {
        if (window.kakao && window.kakao.maps) {
            this.initMap();
        } else {
            const script = document.createElement("script");
            /* global kakao */
            script.onload = () => kakao.maps.load(this.initMap);
            script.src =
                "//dapi.kakao.com/v2/maps/sdk.js?appkey=05df79f7ea0889a7f001a0cb12f76df8&autoload=false&libraries=services";
            document.head.appendChild(script);
        }
    },
    watch: {
        travel_route: {
            handler() {
                if (this.polyline != null) {
                    this.polyline.setMap(null);
                }

                console.log("루트 값 변화");

                var linePath = [];

                for (var route of this.travel_route) {
                    // 위와 같은 동작을 하는 for / of 문
                    linePath.push(new kakao.maps.LatLng(route.latitude, route.longitude));
                }

                // 지도에 표시할 선을 생성합니다
                this.polyline = new kakao.maps.Polyline({
                    path: linePath, // 선을 구성하는 좌표배열 입니다
                    strokeWeight: 5, // 선의 두께 입니다
                    strokeColor: "#FFAE00", // 선의 색깔입니다
                    strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                    strokeStyle: "solid", // 선의 스타일입니다
                });

                this.polyline.setMap(this.map);
            },
            deep: true,
        },
    },
    methods: {
        initMap() {
            let base = this;
            var mapContainer = document.getElementById("map"), // 지도를 표시할 div
                mapOption = {
                    center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표(서울특별시청)
                    level: 4, // 지도의 확대 레벨
                };

            var map = new kakao.maps.Map(mapContainer, mapOption);
            this.map = map;

            var geocoder = new kakao.maps.services.Geocoder();

            // 기본 포지션 좌표.
            var initPosition = new kakao.maps.LatLng(126.911186, 35.1401744);

            // Start of 상대방의 마커 세팅.
            var yourMakerOption = {
                spriteOrigin: new kakao.maps.Point(0, 80),
                spriteSize: new kakao.maps.Size(29, 166),
            };

            var imageSrc = require("./image/yourMarker.png"),
                imageSize = new kakao.maps.Size(31, 41),
                imageOption = { offset: new kakao.maps.Point(15, 40) };

            var yourMarkerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

            var yourMarker = new kakao.maps.Marker({
                position: initPosition,
                image: yourMarkerImage,
            });

            base.yourMarker = yourMarker;
            base.yourMarker.setMap(map);

            // End of 상대방의 마커 세팅.

            // Start of 클릭했을 때 마커 세팅.
            var imageSrc = require("./image/myMarker.png"),
                imageSize = new kakao.maps.Size(31, 41),
                imageOption = { offset: new kakao.maps.Point(15, 40) };

            var myMarkerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

            var myMarker = new kakao.maps.Marker({
                position: map.getCenter(),
                image: myMarkerImage,
            });

            myMarker.setMap(map);

            kakao.maps.event.addListener(map, "click", function (mouseEvent) {
                var latlng = mouseEvent.latLng;

                myMarker.setPosition(latlng);

                base.clickLat = latlng.getLat();
                base.clickLng = latlng.getLng();

                base.sendPoint("CLICK");
            });
            // End of 클릭했을 때 마커 세팅.
            
            // Start of 가이드의 추천 장소 출력.
            this.recommend_location_list.forEach(function (location) {
                geocoder.addressSearch(
                    location.address, // 주소로 좌표 검색.
                    function (result, status) {
                        if (status === kakao.maps.services.Status.OK) {
                            console.log(location.name);
                            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                            var imageSrc = require("./image/recommendMarker.png"),
                                imageSize = new kakao.maps.Size(22, 35),
                                imageOption = { offset: new kakao.maps.Point(11, 35) };

                            let recommendMarkerImage = new kakao.maps.MarkerImage(
                                imageSrc,
                                imageSize,
                                imageOption
                            );

                            let recommendMarker = new kakao.maps.Marker({
                                map: map,
                                position: coords,
                                image: recommendMarkerImage,
                            });
                            recommendMarker.setMap(map);

                            // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다
                            var iwContent = '<div style="padding:5px;">' + location.name + "</div>", // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
                                iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

                            // 인포윈도우를 생성합니다
                            var infowindow = new kakao.maps.InfoWindow({
                                content: iwContent,
                                removable: iwRemoveable,
                            });

                            // 마커에 클릭이벤트를 등록합니다
                            kakao.maps.event.addListener(recommendMarker, "click", function () {
                                // 마커 위에 인포윈도우를 표시합니다
                                base.clickLat = coords.getLat();
                                base.clickLng = coords.getLng();
                                base.sendPoint("CLICK");
                                let index = base.recommend_location_list.findIndex(
                                    (i) => i.name == location.name
                                );
                                base.clickIndex = index;
                                console.log("클릭한 추천 장소 index는 " + base.clickIndex);
                                base.clickRecommend = true;
                                infowindow.open(map, recommendMarker);
                            });
                        } else {
                            console.log("추천 장소 출력이 제대로 안됨 - " + status);
                        }

                        // 일단 추천 장소의 마지막을 기준으로 맵의 중심을 잡는다.
                        map.setCenter(coords);
                    }
                );
            });
            // End of 가이드의 추천 장소 출력.

            // Start of 기존 유명 장소 가져오기.
            let popular_list_index = 0;
            var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

            // 장소 검색 객체를 생성합니다
            var ps = new kakao.maps.services.Places();

            // 키워드로 장소를 검색합니다
            const keyword = this.guideCountry + " " + this.guideCity + " 관광명소";
            console.log("키워드 장소 검색 : "+keyword);
            ps.keywordSearch(keyword, placesSearchCB);

            // 키워드 검색 완료 시 호출되는 콜백함수 입니다
            function placesSearchCB(data, status, pagination) {
                if (status === kakao.maps.services.Status.OK) {
                    for (var i = 0; i < data.length; i++) {
                        displayMarker(data[i]);
                    }
                    popular_list_index += data.leghth;

                    if (pagination.hasNextPage) {
                        pagination.nextPage();
                        for (var i = 0; i < data.length; i++) {
                            displayMarker(data[i]);
                        }
                        popular_list_index += data.leghth;
                    }

                    if (pagination.hasNextPage) {
                        pagination.nextPage();
                        for (var i = 0; i < data.length; i++) {
                            displayMarker(data[i]);
                        }
                        popular_list_index += data.leghth;
                    }
                }
            }

            // 지도에 마커를 표시하는 함수입니다
            function displayMarker(place) {
                var imageSize = new kakao.maps.Size(12, 12), // 마커이미지의 크기입니다
                    imageOption = { offset: new kakao.maps.Point(6, 6) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

                // 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
                var markerImage = new kakao.maps.MarkerImage(
                    base.pointImageSrc,
                    imageSize,
                    imageOption
                );

                // 마커를 생성하고 지도에 표시합니다
                var popularMarker = new kakao.maps.Marker({
                    map: base.map,
                    position: new kakao.maps.LatLng(place.y, place.x),
                    image: markerImage,
                });

                base.popular_marker_list.push(popularMarker);
                base.popular_list.push({
                    name: place.place_name,
                    address: place.address_name,
                    latitude: place.y,
                    longitude: place.x,
                });

                // 마커에 이벤트를 등록하는 함수 만들고 즉시 호출하여 클로저를 만듭니다
                // 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
                (function (popularMarker, infowindow) {
                    // 마커에 mouseover 이벤트를 등록하고 마우스 오버 시 인포윈도우를 표시합니다
                    kakao.maps.event.addListener(popularMarker, "mouseover", function () {
                        infowindow.setContent(
                            '<div style="padding:5px;font-size:12px;">' +
                                place.place_name +
                                "</div>"
                        );
                        infowindow.open(map, popularMarker);
                    });

                    // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
                    kakao.maps.event.addListener(popularMarker, "mouseout", function () {
                        infowindow.close();
                    });
                })(popularMarker, infowindow);

                // 마커에 클릭이벤트를 등록합니다
                kakao.maps.event.addListener(popularMarker, "click", function () {
                    base.clickLat = place.y;
                    base.clickLng = place.x;
                    base.sendPoint("CLICK");
                    let index = base.popular_list.findIndex((i) => i.name == place.place_name);
                    base.clickIndex = index;
                    base.clickPopular = true;
                    console.log("클릭한 유명 장소 index는 " + base.clickIndex);
                });
            }
            // End of 기존 유명 장소 가져오기.

            // ----------------------------------- init map ()
        },
        // [Function] 받은 좌표로 부드럽게 중심을 이동하는 함수.
        panTo() {
            var moveLatLon = new kakao.maps.LatLng(
                parseFloat(this.recvPoint.recvLat),
                parseFloat(this.recvPoint.recvLng)
            );
            this.map.panTo(moveLatLon);
        },
        // [Function] 클릭한 좌표를 전송하는 함수.
        sendPoint(type) {
            this.stompClient.send(
                "/pub/map/point",
                JSON.stringify({
                    roomId: this.roomId,
                    lng: this.clickLng,
                    lat: this.clickLat,
                    type: type,
                })
            );
        },
        // [Function] 상대방이 클릭한 좌표를 받아 내 화면에 보여주는 함수.
        convertRecvPoint(recv) {
            var tmpLng = recv.lng;
            var tmpLat = recv.lat;

            // 내가 클릭한 좌표가 아니라면
            if (tmpLng != this.clickLng && tmpLat != this.clickLng) {
                this.recvPoint = {
                    recvLng: recv.lng,
                    recvLat: recv.lat,
                    type: recv.type,
                };

                // 상대방 마커의 위치를 받은 좌표로 옮긴다.
                var position = new kakao.maps.LatLng(
                    this.recvPoint.recvLat,
                    this.recvPoint.recvLng
                );
                this.yourMarker.setPosition(position);

                // 만약 타입이 포커스라면 내 화면을 그쪽으로 이동시킨다.
                if (recv.type == "FOCUS") {
                    this.panTo();
                }
            }
        },
        // [Function] 웹소켓 연결 함수.
        connect() {
            // var sock = new SockJS("http://localhost:8080/ws-stomp");
            var sock = new SockJS("https://i8c207.p.ssafy.io/ws-stomp");
            this.stompClient = Stomp.over(sock);
            console.log(`소켓 연결을 시도합니다.`);
            // pub/sub event
            this.stompClient.connect(
                {},
                (frame) => {
                    // 소켓 연결 성공
                    console.log("소켓 연결 성공", frame);

                    this.stompClient.subscribe(`/sub/map/room2/${this.roomId}`, (point) => {
                        // console.log("구독으로 받은 좌표 입니다.", point.body);
                        // console.log("구독으로 받은 좌표의 타입은 ", JSON.parse(point.body).type);
                        this.convertRecvPoint(JSON.parse(point.body));
                    });
                    this.stompClient.subscribe(`/sub/map/room3/${this.roomId}`, (route) => {
                        // console.log("구독으로 받은 루트 입니다.", route.body);
                        var routeItem = JSON.parse(route.body);
                        // console.log("루트의 타입은 "+routeItem.type);
                        if (routeItem.type == "UP") {
                            this.upRoute(routeItem);
                        } else if (routeItem.type == "DOWN") {
                            this.downRoute(routeItem);
                        } else if (routeItem.type == "DELETE") {
                            this.deleteRoute(routeItem);
                        } else if (routeItem.type == "SAVE") {
                            this.saveRecvRoute(routeItem);
                        }
                    });
                    this.stompClient.subscribe(`/sub/chat/room/${this.roomId}`, (res) => {
                        this.messages.push(JSON.parse(res.body));
                    });

                    //채팅방 입장(서버에 유저가 들어왔음을 알림)
                    this.stompClient.send(
                        "/pub/chat/message",
                        JSON.stringify({
                            messageType: "ENTER",
                            chatRoomId: this.roomId,
                            writerName: this.userName,
                        })
                    );
                },
                // 소켓 연결 실패
                (error) => {
                    console.log("소켓 연결 실패", error);
                    this.connected = false;
                }
            );
        }, // connect() 끝
        // [Function] 현재 가이드의 추천 장소 데이터를 가져오는 함수.
        async getRecommend() {
            console.log("getRecommend() call.");
            await axios
                .get(APPLICATION_SERVER_URL + `/guides/location/${this.guideId}`)
                .then((res) => {
                    this.recommend_location_list = res.data;
                });
            console.log("추천장소는 "+this.recommend_location_list);
        },
        // [Function] 현재 가이드의 국가와 도시를 가져오는 함수.
        async getCity() {
            console.log("getCity("+this.guidePk+") call.");
            await axios
                .get(APPLICATION_SERVER_URL + `/guides/${this.guidePk}`)
                .then((res) => {
                    this.guideCity = res.data.city;
                    this.guideCountry = res.data.country;
                });
            console.log("현재 가이드의 당담 도시는 "+this.guideCountry+" "+this.guideCity);
        },
        // [Function] 좌표를 이용해서 법정동 주소를 얻는 함수.
        getAddress(lat, lng) {
            var geocoder = new kakao.maps.services.Geocoder();
            return new Promise((resolve, reject) => {
                geocoder.coord2Address(lng, lat, function (result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        resolve(result[0].address.address_name);
                    } else {
                        reject(status);
                    }
                });
            });
        },
        // [Function] 주소를 이용해서 이름을 얻는 함수.
        getPlaceName(address) {
            // 장소 검색 객체를 생성합니다
            var ps = new kakao.maps.services.Places();

            return new Promise((resolve, reject) => {
                ps.keywordSearch(address, function (data, status, pagination) {
                    if (status === kakao.maps.services.Status.OK) {
                        console.log("찍은 주소 이름은 : " + data[0].place_name);
                        resolve(data[0].place_name);
                    } else {
                        resolve(address);
                    }
                });
            });
        },
        // [Function] 좌표에 대한 마커를 생성하는 함수.
        setMarker(lat, lng, markerImage) {
            var markerPosition = new kakao.maps.LatLng(lat, lng);
            var marker = new kakao.maps.Marker({
                position: markerPosition,
                image: markerImage,
            });
            this.route_marker_list.push(marker);
            marker.setMap(this.map);
        },
        // [Function] 현재 클릭한 장소에 대해 travel_route를 추가하기 위해 데이터를 전송하는 함수.
        async saveRoute(index, clickRecommend, clickPopular) {
            console.log("saveRoute() call.");
            var address = await this.getAddress(this.clickLat, this.clickLng);
            var name;

            if (clickRecommend == false && clickPopular == false) {
                name = await this.getPlaceName(address);
            }
            if (clickRecommend == false && clickPopular == true) {
                name = this.popular_list[index].name;
            }
            if (clickRecommend == true && clickPopular == false) {
                name = this.recommend_location_list[index].name;
            }

            console.log("name: " + name);
            this.stompClient.send(
                "/pub/map/route",
                JSON.stringify({
                    roomId: this.roomId,
                    name: name,
                    address: address,
                    latitude: this.clickLat,
                    longitude: this.clickLng,
                    type: "SAVE",
                })
            );
            this.clickIndex = -1;
            this.clickPopular = false;
            this.clickRecommend = false;
        },

        // [Function] 현재의 travel_route 리스트에 대해 선을 생성하는 함수.
        makeLineOfRoute() {
            if (this.polyline != null) {
                this.polyline.setMap(null);
            }

            var linePath = [];

            for (var route of this.travel_route) {
                // 위와 같은 동작을 하는 for / of 문
                linePath.push(new kakao.maps.LatLng(route.latitude, route.longitude));
            }

            console.log(linePath);

            // 지도에 표시할 선을 생성합니다
            this.polyline = new kakao.maps.Polyline({
                path: linePath, // 선을 구성하는 좌표배열 입니다
                strokeWeight: 5, // 선의 두께 입니다
                strokeColor: "#FFAE00", // 선의 색깔입니다
                strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
                strokeStyle: "solid", // 선의 스타일입니다
            });

            this.polyline.setMap(this.map);
        },
        // [Function] 받은 좌표를 저장하는 함수. (맵에 마커도 출력.)
        saveRecvRoute(route) {
            this.travel_route.push({
                name: route.name,
                address: route.address,
                latitude: route.latitude,
                longitude: route.longitude,
            });

            var imageSrc = require("./image/routeMarker.png"),
                imageSize = new kakao.maps.Size(22, 31),
                imageOption = { offset: new kakao.maps.Point(3, 31) };

            var saveMarkerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

            this.setMarker(route.latitude, route.longitude, saveMarkerImage);
        },
        // [Function] 현재의 travel_route 리스트를 DB에 저장하는 함수.
        async insertToDB() {
            await axios
                .post(
                    APPLICATION_SERVER_URL + `/routes/${this.reservationId}`,
                    JSON.stringify(this.travel_route)
                )
                .then(({ data }) => {
                    let msg = "등록 처리시 문제가 발생했습니다.";
                    if (data === "Success") {
                        msg = "등록이 완료되었습니다.";
                    }
                    // console.log(msg);
                });
        },

        // ------------------------------ 여행 일정 조작 함수 --------------------------------

        // [Function] 여행 일정을 관리하는 함수. (타입에 맞춰서 데이터 전송)
        manageRoute(param) {
            var route = this.travel_route[param.index];
            route.type = param.type;
            route.roomId = this.roomId;
            this.stompClient.send("/pub/map/route", JSON.stringify(route));
        },

        // [Function] 여행 일정 삭제.
        deleteRoute(route) {
            const index = this.travel_route.findIndex((i) => i.address == route.address);
            this.route_marker_list[index].setMap(null);
            this.route_marker_list.splice(index, 1);
            this.travel_route.splice(index, 1);
        },

        // [Function] 여행 일정 위로 올리기.
        upRoute(route) {
            const index = this.travel_route.findIndex((i) => i.address == route.address);
            if (index != 0) {
                [this.travel_route[index - 1], this.travel_route[index]] = [
                    this.travel_route[index],
                    this.travel_route[index - 1],
                ];
            }
        },

        // [Function] 여행 일정 내리기.
        downRoute(route) {
            const index = this.travel_route.findIndex((i) => i.address == route.address);
            if (index != this.travel_route.length - 1) {
                [this.travel_route[index + 1], this.travel_route[index]] = [
                    this.travel_route[index],
                    this.travel_route[index + 1],
                ];
            }
        },

        // [Function] 여행 완료로 변경하기.
        async changeState() {
            console.log("changeState "+this.reservationId);
            await axios
                .put(
                    APPLICATION_SERVER_URL + `/books/${this.reservationId}`,
                    JSON.stringify({"status":"RE01"}),
                    {headers: {"Content-Type": 'application/json',}}
                )
                .then(({ data }) => {
                    let msg = "변경 처리시 문제가 발생했습니다.";
                    if (data === "Success") {
                        msg = "변경이 완료되었습니다.";
                    }
                });
        },

        // -----------------------------------------------------------------------------------

        // ------------------------------------ OpenVidu ------------------------------------

        joinSession() {
            // --- 1) Get an OpenVidu object ---
            this.OpenVidu.OV = new OpenVidu();

            // --- 2) Init a session ---
            this.OpenVidu.session = this.OpenVidu.OV.initSession();

            // --- 3) Specify the actions when events take place in the session ---

            // On every new Stream received...
            this.OpenVidu.session.on("streamCreated", ({ stream }) => {
                const subscriber = this.OpenVidu.session.subscribe(stream);
                this.OpenVidu.subscribers.push(subscriber);
            });

            // On every Stream destroyed...
            this.OpenVidu.session.on("streamDestroyed", ({ stream }) => {
                const index = this.OpenVidu.subscribers.indexOf(stream.streamManager, 0);
                if (index >= 0) {
                    this.OpenVidu.subscribers.splice(index, 1);
                }
            });

            // On every asynchronous exception...
            this.OpenVidu.session.on("exception", ({ exception }) => {
                console.warn(exception);
            });

            // --- 4) Connect to the session with a valid user token ---

            // Get a token from the OpenVidu deployment
            this.getToken(this.roomId).then((token) => {
                // First param is the token. Second param can be retrieved by every user on event
                // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
                this.OpenVidu.session
                    .connect(token, { clientData: this.userName })
                    .then(() => {
                        // --- 5) Get your own camera stream with the desired properties ---

                        // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
                        // element: we will manage it on our own) and with the desired properties
                        let publisher = this.OpenVidu.OV.initPublisher(undefined, {
                            audioSource: undefined, // The source of audio. If undefined default microphone
                            videoSource: undefined, // The source of video. If undefined default webcam
                            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
                            publishVideo: true, // Whether you want to start publishing with your video enabled or not
                            resolution: "640x480", // The resolution of your video
                            frameRate: 30, // The frame rate of your video
                            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
                            mirror: false, // Whether to mirror your local video or not
                        });

                        // Set the main video in the page to display our webcam and store our Publisher
                        this.OpenVidu.mainStreamManager = publisher;
                        this.OpenVidu.publisher = publisher;

                        // --- 6) Publish your stream ---

                        this.OpenVidu.session.publish(this.OpenVidu.publisher);
                    })
                    .catch((error) => {
                        console.log(
                            "There was an error connecting to the session:",
                            error.code,
                            error.message
                        );
                    });
            });

            window.addEventListener("beforeunload", this.leaveSession);
        },

        async leaveSession() {
            // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
            if (this.OpenVidu.session) this.OpenVidu.session.disconnect();

            // Empty all properties...
            this.OpenVidu.session = undefined;
            this.OpenVidu.mainStreamManager = undefined;
            this.OpenVidu.publisher = undefined;
            this.OpenVidu.subscribers = [];
            this.OpenVidu.OV = undefined;

            if(this.isGuide == "US1"){
                // 저장 후 종료.
                await this.insertToDB();
                await this.changeState();
            }

            // Remove beforeunload listener
            window.removeEventListener("beforeunload", this.leaveSession);
            window.close();
        },

        updateMainVideoStreamManager(stream) {
            if (this.OpenVidu.mainStreamManager === stream) return;
            this.OpenVidu.mainStreamManager = stream;
        },

        async getToken(mySessionId) {
            const sessionId = await this.createSession(mySessionId);
            return await this.createToken(sessionId);
        },

        async createSession(sessionId) {
            const response = await axios.post(
                APPLICATION_SERVER_URL + "/api/sessions",
                { customSessionId: sessionId },
                {
                    headers: { "Content-Type": "application/json" },
                }
            );
            return response.data; // The sessionId
        },

        async createToken(sessionId) {
            const response = await axios.post(
                APPLICATION_SERVER_URL + "/api/sessions/" + sessionId + "/connections",
                {},
                {
                    headers: { "Content-Type": "application/json" },
                }
            );
            return response.data; // The token
        },

        // ------------------------------------ OpenVidu (end) ------------------------------------

        // ------------------------------------- Chat ---------------------------------------------
        sendMessage() {
            //메시지를 보냄
            this.stompClient.send(
                "/pub/chat/message",
                JSON.stringify({
                    messageType: "CHAT",
                    chatRoomId: this.roomId,
                    writerName: this.userName,
                    message: this.msg,
                })
            );
            this.msg = "";
        },
    },
};
</script>

<style>
[v-cloak] {
    display: none;
}

.route-list {
    padding-bottom: 6px;
    background-color: #15c4cb;
    color: white;
    font-weight: bold;
}

.btn_map {
    display: block;
    overflow: hidden;
    width: 47px;
    height: 16px;
    padding: 9px 0 11px;
    font-weight: normal;
    font-size: 12px;
    line-height: 16px;
    color: #000;
    text-align: center;
    text-decoration: none;
}

#category {
    position: absolute;
    top: 8%;
    left: 5%;
    border-radius: 5px;
    border: 1px solid #909090;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
    background: #fff;
    overflow: hidden;
    z-index: 2;
}

#category li {
    list-style: none;
    width: 50px;
    border-top: 1px solid #acacac;
    padding: 6px 0;
    text-align: center;
    cursor: pointer;
}

.chat {
    border: 1px solid lightgray;
    width: 27%;
    height: 280px;
    float: left;
    margin-left: 10px;
    margin-top: 10px;
}
</style>
