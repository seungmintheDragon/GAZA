<template>
  <div>
    <a
      href="#!"
      @click="modalToggle"
      data-bs-toggle="modal"
      data-bs-target="#exampleModal"
    >
      <button class="btn btn_theme">사진선택</button>
    </a>


    <div
      ref="modal"
      class="modal fade"
      :class="{ show: active, 'd-block': active }"
      tabindex="-1"
      role="dialog"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-body logout_modal_content">
            <div class="btn_modal_closed">
              <button
                type="button"
                data-bs-dismiss="modal"
                @click="modalToggle"
                aria-label="Close"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
            <h3>
              <p> {{ state.changePicture ? state.pictureName  : '변경할 사진을 등록해주세요.' }} </p>

            </h3>
            <div class="logout_approve_button">
              <label data-bs-dismiss="modal" class="btn btn_theme btn_md" for="formFile">사진 선택</label>
              <input class="form-control" type="file" id="formFile" accept="image/*" @change="upload" ref="pictureData" style="display:none">
              <button
                data-bs-dismiss="modal"
                class="btn btn_border btn_md"
                @click="modalToggle"
              >
              닫기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref, getCurrentInstance } from 'vue'

export default {
  emits: ["pictureData"],
  name: "picturemodal",
  data() {
    return {
      active: false,
    };
  },
  methods: {
    modalToggle() {
      const body = document.querySelector("body");
      this.active = !this.active;
      this.active
        ? body.classList.add("modal-open")
        : body.classList.remove("modal-open");
    },
  },

  setup() {
  

    const state = reactive({
      changePicture : false,
      pictureName : ''
    })

    const pictureData = ref(null);


    const { emit } = getCurrentInstance();

    const upload = async() => {
        console.log("selected file", pictureData.value.files[0].name)
        state.pictureName = pictureData.value.files[0].name
        state.changePicture = true;

        emit("pictureData", pictureData.value.files[0]);
    }
    
    return { state, pictureData, upload}
  }
};
</script>
