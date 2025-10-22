<!-- 订单管理 -->
<script setup lang="ts">
defineOptions({
  name: "Order",
  inheritAttrs: false,
});

import { getOrderPage, getOrderDetail } from "@/api/oms/order";
import { Order, OrderQuery } from "@/api/oms/order/types";

const queryFormRef = ref();

const orderSourceMap = {
  1: "APP",
  2: "网页",
};

const orderStatusMap = {
  101: "待付款",
  102: "用户取消",
  103: "系统取消",
  201: "已付款",
  202: "申请退款",
  203: "已退款",
  301: "待发货",
  401: "已发货",
  501: "用户收货",
  502: "系统收货",
  901: "已完成",
};

const payTypeMap = {
  1: "支付宝",
  2: "微信",
  3: "会员余额",
};

const state = reactive({
  loading: false,
  ids: [],
  single: true,
  multiple: true,
  dateRange: [] as any,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
  } as OrderQuery,
  orderList: [] as Order[],
  total: 0,
  dialog: {
    title: "订单详情",
    visible: false,
  } as DialogType,
  dialogVisible: false,
  orderDetail: {
    id: undefined as number | undefined,
    orderSn: undefined as string | undefined,
    createTime: undefined as string | undefined,
    paymentTime: undefined as string | undefined,
    deliveryTime: undefined as string | undefined,
    receiveTime: undefined as string | undefined,
    order: {
      refundAmount: undefined as number | undefined,
      refundType: undefined as number | undefined,
      refundNote: undefined as string | undefined,
      gmtRefund: undefined as string | undefined,
      confirmTime: undefined as string | undefined,
      gmtDelivery: undefined as string | undefined,
      shipSn: undefined as string | undefined,
      shipChannel: undefined as string | undefined,
      gmtPay: undefined as string | undefined,
      integralPrice: undefined as number | undefined,
      payChannel: undefined as number | undefined,
      skuPrice: undefined as number | undefined,
      couponPrice: undefined as number | undefined,
      freightPrice: undefined as number | undefined,
      orderPrice: undefined as number | undefined,
      totalAmount: undefined as number | undefined,
      paymentAmount: undefined as number | undefined,
      status: undefined as number | undefined,
      source: undefined as number | undefined,
    },
    member: {} as any,
    orderItems: [] as any[],
  },
  orderSourceMap,
  orderStatusMap,
  payTypeMap,
});

const { loading, queryParams, orderList, total, dateRange } = toRefs(state);

function handleQuery() {
  state.loading = true;
  getOrderPage(state.queryParams).then(({ data }) => {
    state.orderList = data.list;
    state.total = data.total;
    state.loading = false;
  });
}

function resetQuery() {
  queryFormRef.value.resetFields();
  handleQuery();
}

// 分转元函数
function fenToYuan(fen: number | string): string {
  if (fen === null || fen === undefined || fen === "") return "0.00";
  const num = typeof fen === "string" ? parseFloat(fen) : fen;
  if (isNaN(num)) return "0.00";
  return (num / 100).toFixed(2);
}

function viewDetail(row: any) {
  state.dialog.visible = true;
  getOrderDetail(row.id).then((response: any) => {
    state.orderDetail = response.data;
  });
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="orderSn">
          <el-input v-model="queryParams.orderSn" placeholder="订单号" />
        </el-form-item>

        <el-form-item>
          <el-date-picker
            v-model="dateRange"
            style="width: 240px"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>

        <el-form-item>
          <el-select
            v-model="queryParams.status"
            class="filter-item"
            placeholder="订单状态"
          >
            <el-option
              v-for="(key, value) in orderStatusMap"
              :key="key"
              :label="key"
              :value="value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />查询</el-button
          >
          <el-button @click="resetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table ref="dataTable" v-loading="loading" :data="orderList" border>
      <el-table-column type="expand" width="100" label="订单商品">
        <template #default="scope">
          <el-table
            :data="scope.row.orderItem ? [scope.row.orderItem] : []"
            border
          >
            <el-table-column label="序号" type="index" width="100" />
            <el-table-column label="商品编号" align="center" prop="skuSn" />
            <el-table-column label="商品规格" align="center" prop="skuName" />
            <el-table-column label="图片" prop="picUrl">
              <template #default="item">
                <img :src="item.row.picUrl" width="40" />
              </template>
            </el-table-column>
            <el-table-column align="center" label="单价" prop="price">
              <template #default="item">{{
                fenToYuan(item.row.price)
              }}</template>
            </el-table-column>
            <el-table-column align="center" label="数量" prop="count">
              <template #default="item">{{ item.row.count }}</template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="orderSn" label="订单编号" />

      <el-table-column align="center" prop="memberId" label="会员ID" />

      <el-table-column align="center" label="订单来源">
        <template #default="scope">
          <el-tag>{{
            orderSourceMap[scope.row.source as keyof typeof orderSourceMap]
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="订单状态">
        <template #default="scope">
          <el-tag>{{
            orderStatusMap[scope.row.status as keyof typeof orderStatusMap]
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="orderPrice" label="订单金额">
        <template #default="scope">
          {{ fenToYuan(scope.row.totalAmount) }}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="payPrice" label="支付金额">
        <template #default="scope">
          {{ fenToYuan(scope.row.paymentAmount) }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="支付方式">
        <template #default="scope">
          <el-tag>{{ scope.row.paymentMethod }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" prop="createTime" label="创建时间" />

      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button @click="viewDetail(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页工具条 -->
    <pagination
      v-if="total > 0"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      :total="total"
      @pagination="handleQuery"
    />

    <!-- 订单详情弹窗 -->
    <el-dialog
      :title="state.dialog.title"
      v-model="state.dialog.visible"
      width="800px"
      append-to-body
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{
          state.orderDetail?.orderSn
        }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">{{
          fenToYuan(state.orderDetail?.order?.totalAmount || 0)
        }}</el-descriptions-item>
        <el-descriptions-item label="支付金额">{{
          fenToYuan(state.orderDetail?.order?.paymentAmount || 0)
        }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{
          orderStatusMap[
            (state.orderDetail?.order?.status ||
              101) as keyof typeof orderStatusMap
          ]
        }}</el-descriptions-item>
        <el-descriptions-item label="订单来源">{{
          orderSourceMap[
            (state.orderDetail?.order?.source ||
              1) as keyof typeof orderSourceMap
          ]
        }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{
          state.orderDetail?.createTime
        }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{
          state.orderDetail?.paymentTime
        }}</el-descriptions-item>
        <el-descriptions-item label="发货时间">{{
          state.orderDetail?.deliveryTime
        }}</el-descriptions-item>
        <el-descriptions-item label="确认收货时间">{{
          state.orderDetail?.receiveTime
        }}</el-descriptions-item>
      </el-descriptions>

      <!-- 订单商品信息 -->
      <el-divider content-position="left">订单商品</el-divider>
      <el-table :data="state.orderDetail?.orderItems" border>
        <el-table-column label="商品图片" width="100">
          <template #default="scope">
            <img :src="scope.row.picUrl" width="40" />
          </template>
        </el-table-column>
        <el-table-column label="商品名称" prop="spuName" />
        <el-table-column label="单价" prop="price">
          <template #default="scope">
            {{ fenToYuan(scope.row.price) }}
          </template>
        </el-table-column>
        <el-table-column label="数量" prop="quantity" />
        <el-table-column label="小计">
          <template #default="scope">
            {{ fenToYuan(scope.row.price * scope.row.quantity) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 会员信息 -->
      <el-divider
        content-position="left"
        v-if="
          state.orderDetail.member &&
          Object.keys(state.orderDetail.member).length > 0
        "
        >会员信息</el-divider
      >
      <el-descriptions
        :column="2"
        border
        v-if="
          state.orderDetail.member &&
          Object.keys(state.orderDetail.member).length > 0
        "
      >
        <el-descriptions-item label="会员ID">{{
          (state.orderDetail.member as any)?.id
        }}</el-descriptions-item>
        <el-descriptions-item label="会员昵称">{{
          (state.orderDetail.member as any)?.nickName
        }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{
          (state.orderDetail.member as any)?.mobile
        }}</el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="state.dialog.visible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
