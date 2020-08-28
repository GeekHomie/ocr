<template>
  <div class="result">
    <a-descriptions title="图片识别结果" bordered :column="2" class="result-area">
      <a-descriptions-item label="商旅申请单号">{{approvalFormNumber}}</a-descriptions-item>
      <a-descriptions-item label="工号">{{jobNumber}}</a-descriptions-item>
      <a-descriptions-item label="出差人">
        <a-tag
          v-for="item in name"
          :key="item"
        >{{item}}</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="出差事由">{{travelReason}}</a-descriptions-item>
      <a-descriptions-item label="出差地点">{{travelLocation}}</a-descriptions-item>
      <a-descriptions-item label="地区分类">{{locationType}}</a-descriptions-item>
      <a-descriptions-item label="出差类型">{{travelLocationType===0?"行外":"行外"}}</a-descriptions-item>
      <a-descriptions-item label="员工类型">{{employeeType===0?"行员":"厂商"}}</a-descriptions-item>
      <a-descriptions-item label="出发日期">{{departureDate}}</a-descriptions-item>
      <a-descriptions-item label="结束日期">{{arrivalDate}}</a-descriptions-item>
      <a-descriptions-item label="城际交通费">{{transportFee}}</a-descriptions-item>
      <a-descriptions-item label="住宿费">{{accommodationFee}}</a-descriptions-item>
    </a-descriptions>
    <a-descriptions title="系统审核结果" bordered :column="2" class="result-area">
      
      <a-descriptions-item label="出差天数">{{travelDays}}</a-descriptions-item>
      <a-descriptions-item label="出发到达日期是否对应" >{{isTravelDaysConsistent?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="审批单费用是否对应">{{isFeeConsistent?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="姓名是否对应">{{isNameConsistent?"是":"否"}}</a-descriptions-item>
      <a-descriptions-item label="交通补贴">{{transportAllowance}}</a-descriptions-item>
      <a-descriptions-item label="餐补金额">{{mealAllowance}}</a-descriptions-item>
      <a-descriptions-item label="审批单申请总金额">{{approvalFormFee}}</a-descriptions-item>
    <a-descriptions-item label="报销总金额">{{allFee}}</a-descriptions-item>
    </a-descriptions>
    <div class="button-area">
      <a-button class="btn" type="primary" size="large" @click="backUploadPic">返回上传图片页面</a-button>
      <a-button class="btn" type="primary" size="large">生成并导出Excel文件</a-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      approvalFormNumber: 'SS202008281234',//商旅单号，string
      jobNumber: '304074',                 //工号， string
      name: ['石伟', '钱德鑫'],             //出差人，array
      travelReason: '极客大赛',             //出差事由， string
      travelLocation: '张江',               //出差地点， string，
      locationType: 'BX00401',              //地区分类， 后端在“BX00401”和"BX00302"中随机一个
      travelLocationType: 0,                //出差类型，0和1枚举，0代表行内，1代表行外
      departureDate: '2020-08-28',          //出发日期，string
      arrivalDate: '2020-08-31',            //到达日期，string
      transportFee: 150.00,                  //城际交通费， double数字型
      accommodationFee: 6000.00,             //住宿费，  double数字型
      employeeType:0,                       //员工类型编码，0代表行员，1代表厂商
    
      travelDays: 3,                        //出差天数，Int型
      isTravelDaysConsistent:true,           //出差天数与车票日期是否对应, boolean
      isNameConsistent:true,                //姓名核验，boolean
      isFeeConsistent:true,                 //审批单费用是否一致，boolean
      transportAllowance:240,               //交通补贴,天数乘以80，
      mealAllowance:150,                    //餐补，天数乘以50，
      approvalFormFee:6150.00,              //审批单的住宿费和城际交通费,
      allFee:6540                            //所有报销金额相加， 6150+150+240
    }
  },
  methods: {
    backUploadPic() {
      this.$router.push({ path: '/reimbursement/travel' })
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
