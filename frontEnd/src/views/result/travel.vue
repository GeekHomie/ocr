<template>
  <div class="result">
    <a-descriptions title="审批单识别结果" bordered :column="2" class="result-area">
      <a-descriptions-item label="商旅申请单号">{{approvalFormNumber}}</a-descriptions-item>
      <a-descriptions-item label="工号">{{jobNumber}}</a-descriptions-item>
      <a-descriptions-item label="出差人">{{name}}</a-descriptions-item>
      <a-descriptions-item label="出差事由">{{travelReason}}</a-descriptions-item>
      <a-descriptions-item label="出差地点">{{travelLocation}}</a-descriptions-item>
      <a-descriptions-item label="地区分类">{{locationType}}</a-descriptions-item>
      <a-descriptions-item label="出差类型">{{travelLocationType===0?"行外":"行外"}}</a-descriptions-item>
      <a-descriptions-item label="员工类型">{{employeeType===0?"行员":"厂商"}}</a-descriptions-item>
      <a-descriptions-item label="出发日期">{{departureDate | datejs}}</a-descriptions-item>
      <a-descriptions-item label="结束日期">{{arrivalDate | datejs}}</a-descriptions-item>
      <a-descriptions-item label="城际交通费">{{transportFee | money}}</a-descriptions-item>
      <a-descriptions-item label="住宿费">{{accommodationFee | money}}</a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="系统校验审核结果" bordered :column="2" class="result-area">
      <a-descriptions-item label="出差天数">{{travelDays}}</a-descriptions-item>
      <a-descriptions-item label="出发到达日期是否对应">{{travelDaysConsisted?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="审批单费用是否对应">{{feeConsisted?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="姓名是否对应">{{nameConsisted?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="交通补贴">{{transportAllowance | money}}</a-descriptions-item>
      <a-descriptions-item label="餐补金额">{{mealAllowance | money}}</a-descriptions-item>
      <a-descriptions-item label="审批单申请总金额">{{approvalFormFee | money}}</a-descriptions-item>
      <a-descriptions-item label="报销总金额">{{allFee | money}}</a-descriptions-item>
    </a-descriptions>
    <div class="button-area">
      <a-button class="btn" type="primary" size="large" @click="backUploadPic">返回上传图片页面</a-button>
      <a-button class="btn" type="primary" size="large" @click="generateExcel">生成并导出Excel文件</a-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      approvalFormNumber: '', //商旅单号，string
      jobNumber: '', //工号， string
      name: [], //出差人，array
      travelReason: '', //出差事由， string
      travelLocation: '', //出差地点， string，
      locationType: '', //地区分类， 后端在“BX00401”和"BX00302"中随机一个
      travelLocationType: 0, //出差类型，0和1枚举，0代表行内，1代表行外
      departureDate: '', //出发日期，string
      arrivalDate: '', //到达日期，string
      transportFee: 0, //城际交通费， double数字型
      accommodationFee: 0, //住宿费，  double数字型
      employeeType: 0, //员工类型编码，0代表行员，1代表厂商

      travelDays: 0, //出差天数，Int型
      travelDaysConsisted: true, //出差天数与车票日期是否对应, boolean
      nameConsisted: true, //姓名核验，boolean
      feeConsisted: true, //审批单费用是否一致，boolean
      transportAllowance: 0, //交通补贴,天数乘以80，
      mealAllowance: 0, //餐补，天数乘以50，
      approvalFormFee: 0, //审批单的住宿费和城际交通费,
      allFee: 0, //所有报销金额相加， 6150+150+240

      uuid: '', //用于生成excel
    }
  },
  created() {
    const { approvalUid, ticketUid } = this.$route.query
    axios({
      url: 'http://116.62.138.168:8000/check',
      method: 'post',
      params: {
        tableId: approvalUid,
        imgId: ticketUid,
      },
    }).then((res) => {
      this.approvalFormNumber = res.data.approvalFormNumber
      this.travelLocation = res.data.travelLocation
      this.jobNumber = res.data.jobNumber
      this.name = res.data.name
      this.travelReason = res.data.travelReason
      this.locationType = res.data.locationType
      this.travelLocationType = res.data.travelLocationType
      this.departureDate = res.data.departureDate
      this.arrivalDate = res.data.arrivalDate
      this.accommodationFee = res.data.accommodationFee
      this.employeeType = res.data.employeeType
      this.travelDays = res.data.travelDays
      this.travelDaysConsisted = res.data.travelDaysConsisted
      this.nameConsisted = res.data.nameConsisted
      this.feeConsisted = res.data.feeConsisted
      this.transportAllowance = res.data.transportAllowance
      this.mealAllowance = res.data.mealAllowance
      this.approvalFormFee = res.data.approvalFormFee
      this.allFee = res.data.allFee
      this.uuid = res.data.uuid
    })
  },
  methods: {
    backUploadPic() {
      this.$router.push({ path: '/reimbursement/travel' })
    },
    generateExcel() {
      console.log(this.travelDaysConsisted)
      console.log(this.nameConsisted)
      console.log(this.feeConsisted)
      console.log(this.uuid)
      axios({
        url: 'http://116.62.138.168:8000/excel',
        method: 'post',
        params: {
          uuid: this.uuid,
        },
        responseType: 'blob',
      }).then((res) => {
        // Blob 对象表示一个不可变、原始数据的类文件对象（File 接口都是基于Blob）
        const BLOB = res.data
        // FileReader 对象允许Web应用程序异步读取存储在用户计算机上的文件的内容
        const fileReader = new FileReader()
        // 开始读取指定的Blob中的内容。读取完成result属性中将包含一个data: URL格式的Base64字符串以表示所读取文件的内容
        fileReader.readAsDataURL(BLOB)
        // 处理load事件，该事件在读取操作完成时触发
        fileReader.onload = (event) => {
          let a = document.createElement('a')
          a.download = `出差报销审核结果.xls`
          a.href = event.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.result {
  .result-area {
    margin: 15px;
  }
  .button-area {
    display: flex;
    justify-content: center;
    align-items: center;
    .btn {
      margin: 20px;
      width: 200px;
    }
  }
}
</style>





