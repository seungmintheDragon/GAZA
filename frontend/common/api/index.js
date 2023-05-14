import axios from "axios";

function apiInstance() {
  const instance = axios.create({
    baseURL: 'https://i8c207.p.ssafy.io',
  });
  return instance;
}

export { apiInstance,};
