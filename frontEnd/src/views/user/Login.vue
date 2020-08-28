<template>
  <div class="main">
    <div class="top">
      <div class="logo">
        <img src="~@/assets/bosc_logo.png" alt="logo" />
      </div>
      <div class="title">
        <span>上海银行报销系统</span>
      </div>
    </div>
    <a-input size="large" class="login-input" v-model="username" :allowClear="true" placeholder="请输入用户名" />
    <a-input-password size="large" class="login-input" v-model="password" :allowClear="true" placeholder="请输入密码" />
    <a-button type="primary" class="login-btn" size="large" :block="true" @click="handleLogin">登录</a-button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
        username:'',
        password:'',
    }
  },
  methods: {
      handleLogin:function(){
          axios({
              url:'http://116.62.138.168:8000/login',
              method:'post',
              params:{
                  username:this.username,
                  password:this.password
              }
          }).then(res=>{
              if(res.data.ret=="err"){
                  this.$message.error('用户名密码错误，请重新输入');
                  this.username = '';
                  this.password = '';
              }
              else if(res.data.ret=="ok"){
                  this.$router.push('/reimbursement/travel')
              }
              else{
                  console.log('others')
              }
          })
      }
  },
}
</script>

<style lang="less" scoped>
.main {
  .top {
    margin-top: 30%;
    display: flex;
    .logo {
      width: 20%;
      img{
          height:48px;
      }
    }
    .title {
      width: 70%;
      height: 48px;
      font-size: 30px;
    }
  }
  .login-input {
    margin-top: 24px;
    margin-bottom: 6px;
  }
  .login-btn {
    margin-top: 24px;
  }
}
</style>

