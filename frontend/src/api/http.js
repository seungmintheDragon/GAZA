import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "https://i8c207.p.ssafy.io/api",
  //baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});
