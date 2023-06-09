<template>
  <div id="main-container" class="container">
    <div id="join" v-if="!session">
      <div id="img-div">
        <img src="resources/images/openvidu_grey_bg_transp_cropped.png" />
      </div>
      <div id="join-dialog" class="jumbotron vertical-center">
        <h1>Join a video session</h1>
        <div class="form-group">
          <p>
            <label>Participant</label>
            <input v-model="myUserName" class="form-control" type="text" required />
          </p>
          <p>
            <label>Session</label>
            <input v-model="mySessionId" class="form-control" type="text" required />
          </p>
          <p class="text-center">
            <button class="btn btn-lg btn-success" @click="joinSession()">
              Join!
            </button>
          </p>
        </div>
      </div>
    </div>

    <div id="session" v-if="session">
      <div id="session-header">
        <h1 id="session-title">{{ mySessionId }}</h1>
        <input class="btn btn-large btn-danger" type="button" id="buttonLeaveSession" @click="leaveSession"
          value="Leave session" />
      </div>
      <div id="main-video" class="col-md-6">
        <user-video :stream-manager="mainStreamManager" />
      </div>
      <div id="video-container" class="col-md-6">
        <user-video :stream-manager="publisher" @click="updateMainVideoStreamManager(publisher)" />
        <user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub"
          @click="updateMainVideoStreamManager(sub)" />
      </div>
    </div>
  </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
import axios from "axios";
import UserVideo from "../openvidu/UserVideo";

axios.defaults.headers.common['Authorization'] = "Basic T1BFTlZJRFVBUFA6R0FaQQ==";
axios.defaults.headers.post["Content-Type"] = "application/json";

const APPLICATION_SERVER_URL = "https://i8c207.p.ssafy.io:8443/openvidu/";

export default {
  name: "OpenViduViewCopy",

  components: {
    UserVideo,
  },

  data() {
    return {
      // OpenVidu objects
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],

      // Join form
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100),
    };
  },

  methods: {
    joinSession() {
      // --- 1) Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- 2) Init a session ---
      this.session = this.OV.initSession();

      // --- 3) Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- 4) Connect to the session with a valid user token ---

      // Get a token from the OpenVidu deployment
      this.getToken(this.mySessionId).then((token) => {
        console.log('token',token.token);
        // First param is the token. Second param can be retrieved by every user on event
        // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
        this.session.connect(token.token, { clientData: this.myUserName })
          .then(() => {

            // --- 5) Get your own camera stream with the desired properties ---

            // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
            // element: we will manage it on our own) and with the desired properties
            let publisher = this.OV.initPublisher(undefined, {
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
            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- 6) Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log("There was an error connecting to the session:", error.code, error.message);
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      // Empty all properties...
      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      // Remove beforeunload listener
      window.removeEventListener("beforeunload", this.leaveSession);
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    /**
     * --------------------------------------------
     * GETTING A TOKEN FROM YOUR APPLICATION SERVER
     * --------------------------------------------
     * The methods below request the creation of a Session and a Token to
     * your application server. This keeps your OpenVidu deployment secure.
     * 
     * In this sample code, there is no user control at all. Anybody could
     * access your application server endpoints! In a real production
     * environment, your application server must identify the user to allow
     * access to the endpoints.
     * 
     * Visit https://docs.openvidu.io/en/stable/application-server to learn
     * more about the integration of OpenVidu in your application server.
     */
    async getToken(mySessionId) {
      // const sessionId = await this.createSession(mySessionId);
      // console.log('sessionID');
      // console.log(sessionId);
      return await this.createToken(mySessionId);
    },

    async createSession(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
        headers: { Authorization: 'Basic T1BFTlZJRFVBUFA6R0FaQQ==', 'Content-Type': 'application/json', },
      });
      console.log('=====================================');
      console.log(response.data);
      return response.data; // The sessionId
    },

    async createToken(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connection', {}, {
        headers: {  Authorization: 'Basic T1BFTlZJRFVBUFA6R0FaQQ==', 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' },
      });
      console.log('-------------------------');
      console.log(sessionId);
      console.log(response.data.token);
      return response.data; // The token
    },
  },
};
</script>
<style scoped>
html {
	position: relative;
	min-height: 100%;
}

nav {
	height: 50px;
	width: 100%;
	z-index: 1;
	background-color: #4d4d4d !important;
	border-color: #4d4d4d !important;
	border-top-right-radius: 0 !important;
	border-top-left-radius: 0 !important;
}

.navbar-header {
	width: 100%;
}

.nav-icon {
	padding: 5px 15px 5px 15px;
	float: right;
}

nav a {
	color: #ccc !important;
}

nav i.fa {
	font-size: 40px;
	color: #ccc;
}

nav a:hover {
	color: #a9a9a9 !important;
}

nav i.fa:hover {
	color: #a9a9a9;
}

#main-container {
	padding-bottom: 80px;
}

/*vertical-center {
	position: relative;
	top: 30%;
	left: 50%;
	transform: translate(-50%, -50%);
}*/

.horizontal-center {
	margin: 0 auto;
}

.form-control {
	color: #0088aa;
	font-weight: bold;
}

.form-control:focus {
	border-color: #0088aa;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(0, 136, 170, 0.6);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(0, 136, 170, 0.6);
}

input.btn {
	font-weight: bold;
}

.btn {
	font-weight: bold !important;
}

.btn-success {
	background-color: #06d362 !important;
	border-color: #06d362;
}

.btn-success:hover {
	background-color: #1abd61 !important;
	border-color: #1abd61;
}

.footer {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 60px;
	background-color: #4d4d4d;
}

.footer .text-muted {
	margin: 20px 0;
	float: left;
	color: #ccc;
}

.openvidu-logo {
	height: 35px;
	float: right;
	margin: 12px 0;
	-webkit-transition: all 0.1s ease-in-out;
	-moz-transition: all 0.1s ease-in-out;
	-o-transition: all 0.1s ease-in-out;
	transition: all 0.1s ease-in-out;
}

.openvidu-logo:hover {
	-webkit-filter: grayscale(0.5);
	filter: grayscale(0.5);
}

.demo-logo {
	margin: 0;
	height: 22px;
	float: left;
	padding-right: 8px;
}

a:hover .demo-logo {
	-webkit-filter: brightness(0.7);
	filter: brightness(0.7);
}

#join-dialog {
	margin-left: auto;
    margin-right: auto;
    max-width: 70%;
}

#join-dialog h1 {
	color: #4d4d4d;
	font-weight: bold;
	text-align: center;
}

#img-div {
	text-align: center;
	margin-top: 3em;
	margin-bottom: 3em;
	/*position: relative;
	top: 20%;
	left: 50%;
	transform: translate(-50%, -50%);*/
}

#img-div img {
	height: 15%;
}

#join-dialog label {
	color: #0088aa;
}

#join-dialog input.btn {
	margin-top: 15px;
}

#session-header {
	margin-bottom: 20px;
}

#session-title {
	display: inline-block;
}

#buttonLeaveSession {
	float: right;
	margin-top: 20px;
}

#video-container video {
	position: relative;
	float: left;
	width: 50%;
	cursor: pointer;
}

#video-container video + div {
	float: left;
	width: 50%;
	position: relative;
	margin-left: -50%;
}

#video-container p {
	display: inline-block;
	background: #f8f8f8;
	padding-left: 5px;
	padding-right: 5px;
	color: #777777;
	font-weight: bold;
	border-bottom-right-radius: 4px;
}

video {
	width: 100%;
	height: auto;
}

#main-video p {
	position: absolute;
	display: inline-block;
	background: #f8f8f8;
	padding-left: 5px;
	padding-right: 5px;
	font-size: 22px;
	color: #777777;
	font-weight: bold;
	border-bottom-right-radius: 4px;
}

#session img {
	width: 100%;
	height: auto;
	display: inline-block;
	object-fit: contain;
	vertical-align: baseline;
}

#session #video-container img {
	position: relative;
	float: left;
	width: 50%;
	cursor: pointer;
	object-fit: cover;
	height: 180px;
}


/* xs ans md screen resolutions*/

@media screen and (max-width: 991px) and (orientation: portrait) {
	#join-dialog {
		max-width: inherit;
	}
	#img-div img {
		height: 10%;
	}
	#img-div {
		margin-top: 2em;
		margin-bottom: 2em;
	}
	.container-fluid>.navbar-collapse, .container-fluid>.navbar-header, .container>.navbar-collapse, .container>.navbar-header {
		margin-right: 0;
		margin-left: 0;
	}
	.navbar-header i.fa {
		font-size: 30px;
	}
	.navbar-header a.nav-icon {
		padding: 7px 3px 7px 3px;
	}
}

@media only screen and (max-height: 767px) and (orientation: landscape) {
	#img-div {
		margin-top: 1em;
		margin-bottom: 1em;
	}
	#join-dialog {
		max-width: inherit;
	}
}
</style>
