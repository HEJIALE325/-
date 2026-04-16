<template>
  <div id="app">
    <router-view />
    <Message />
    <Confirm />
    <ChatComponent />
  </div>
</template>

<script setup>
// App根组件
import Message from './components/Message.vue'
import Confirm from './components/Confirm.vue'
import ChatComponent from './components/ChatComponent.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>