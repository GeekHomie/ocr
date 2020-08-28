<template>
  <div class="area">
    <div class="travel">
      <div class="travel-box" id="travel-box-left">
        <a-upload
          class="upload-file"
          name="img"
          accept=".jpg, .jpeg, .png"
          listType="text"
          action="http://116.62.138.168:8000/ocr"
          :fileList="approvalFileList"
          :before-upload="beforeApprovalUpload"
          @change="handleApprovalChange"
          :remove="handleApprovalRemove"
        >
          <a-button icon="upload">上传审批单</a-button>
        </a-upload>
      </div>
      <div class="travel-box" id="travel-box-left">
        <a-upload
          class="upload-file"
          name="img"
          accept=".jpg, .jpeg, .png"
          listType="picture"
          action="http://116.62.138.168:8000/ocr"
          :fileList="ticketFileList"
          :before-upload="beforeTicketUpload"
          @change="handleTicketChange"
          :remove="handleTicketRemove"
        >
          <a-button icon="upload">上传混贴票据</a-button>
        </a-upload>
      </div>
    </div>
    <div class="button-area">
      <a-button size="large" type="primary" @click="handleDataList">生成结果表格</a-button>
    </div>
  </div>
</template>
<script>
import axios from 'axios'

function getBase64(img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

export default {
  data() {
    return {
      approvalLoading: false,
      approvalFileList: [],
      approvalImageUrl: '',
      ticketLoading: false,
      ticketFileList: [],
      ticketImageUrl: '',
    }
  },
  methods: {
    beforeApprovalUpload(file) {
      const temp = [...this.approvalFileList]
      if (temp.length > 0) {
        //已经有一张审批单
        this.$message.warning('只能上传一张审批单，请先删除原来的审批单')
      }
      const numEqualOne = temp.length == 0
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('审批单大小需要小于2MB')
      }
      return isLt2M && numEqualOne
    },

    handleApprovalChange(info) {
      const temp = [...this.approvalFileList]
      if (temp.length == 0) {
        //添加新审批单
        this.approvalFileList = info.fileList
      }
      if (info.file.status === 'uploading') {
        this.approvalLoading = true
        return
      }
      if (info.file.status === 'done') {
        getBase64(info.file.originFileObj, (imageUrl) => {
          this.$message.success('审批单解析成功')
          this.approvalImageUrl = imageUrl
          this.approvalLoading = false
        })
      }
    },

    handleApprovalRemove() {
      this.approvalFileList = []
      this.$message.success('成功移除审批单')
    },

    beforeTicketUpload(file) {
      const temp = [...this.ticketFileList]
      if (temp.length > 0) {
        //已经有一张审批单
        this.$message.warning('只能上传一张混贴票据，请先删除原来的混贴票据')
      }
      const numEqualOne = temp.length == 0
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('混贴票据大小需要小于2MB')
      }
      return isLt2M && numEqualOne
    },

    handleTicketChange(info) {
      const temp = [...this.ticketFileList]
      if (temp.length == 0) {
        //添加新审批单
        this.ticketFileList = info.fileList
      }
      if (info.file.status === 'uploading') {
        this.ticketLoading = true
        return
      }
      if (info.file.status === 'done') {
        getBase64(info.file.originFileObj, (imageUrl) => {
          this.$message.success('混贴票据解析成功')
          this.ticketImageUrl = imageUrl
          this.ticketLoading = false
        })
      }
    },

    handleTicketRemove() {
      this.ticketFileList = []
      this.$message.success('成功移除混贴票据')
    },
    handleDataList(){
        this.$router.push("/result/travel")
    }
  },
}
</script>
<style lang="less" scoped>
.area {
  width: 100%;
  height: 700px;
  background-color: #ffffff;
  .travel {
    width: 100%;
    height: 600px;
    display: flex;
    justify-content: center;
    align-items: center;
    .travel-box {
      background-color: #fffafa;
      width: 30%;
      height: 300px;
      margin-left: 45px;
      margin-right: 45px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  .button-area {
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>









