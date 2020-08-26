<template>
  <div class="travel">
      该功能暂未上线，敬请期待
  </div>
</template>
<script>

import {login} from '@/api/login'

function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result));
  reader.readAsDataURL(img);
}
export default {
  data() {
    return {
      loading: false,
      imageUrl: '',
      username: 'shiwei',
      password: 'test'
    };
  },
  methods: {
      testClick:function(){
          login({username:this.username,password:this.password}).then(res=>{console.log(res)}).catch(err=>{console.log(err)})
      },
    handleChange(info) {
      if (info.file.status === 'uploading') {
        this.loading = true;
        return;
      }
      if (info.file.status === 'done') {
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, imageUrl => {
          this.imageUrl = imageUrl;
          this.loading = false;
        });
      }
    },
    beforeUpload(file) {
    //   const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    //   if (!isJpgOrPng) {
    //     this.$message.error('You can only upload JPG file!');
    //   }
      console.log(file)
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('Image must smaller than 2MB!');
      }
      return isLt2M;
    },
  },
};
</script>
<style lang="less" scoped>
    .travel{
        width: 100%;
        height: 700px;
        background-color: #FFFFFF;
        display: flex;
        justify-content: center;
        align-items: center;
        .travel-box{
            width: 40%;
            height:500px;
            margin-left: 45px;
            margin-right: 45px;
        }
    }
</style>









