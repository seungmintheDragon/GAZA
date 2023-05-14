<template>
    <div class="d-flex justify-content-between list-group-item list-group-item-action">
        <span>{{ index }}</span><span>{{ route.name }}</span>
        <div class="btn-group" role="group" aria-label="Basic example">
            <button type="button" class="btn btn-outline-secondary btn-sm" @click="manageRoute('UP')">▲</button>
            <button type="button" class="btn btn-outline-secondary btn-sm" @click="manageRoute('DOWN')">▼</button>
            <button type="button" class="btn btn-outline-secondary btn-sm" @click="manageRoute('DELETE')">X</button>
        </div>
    </div>
</template>
  
  <script>
  export default {
    props: {
        index: Number,
        route: Object,
    },
    methods: {
        manageRoute(type){
            console.log("manageRoute Call.");
            this.$emit("manageRoute", {index: this.index, type: type});
        },
    },
  };
  </script>

  <style scoped>
  .route-button {
    margin-right: 3px;
  }

  .btn {
    font-size: 8px;
  }
  </style>