<!-- 会员管理 -->
<script setup lang="ts">
defineOptions({
  name: "Member",
  inheritAttrs: false,
});

import {
  getMemberPage,
  getMemberDetail,
  addMember,
  updateMember,
  deleteMembers,
  getMemberAddresses,
  addMemberAddress,
  updateMemberAddress,
  deleteMemberAddresses,
  setDefaultAddress,
} from "@/api/ums/member";
import {
  Member,
  MemberQuery,
  MemberForm,
  AddressForm,
} from "@/api/ums/member/types";

const state = reactive({
  // 遮罩层
  loading: true,
  // 选中数组
  ids: [],
  // 非单个禁用
  single: true,
  // 非多个禁用
  multiple: true,
  total: 0,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
  } as MemberQuery,
  memberList: [] as Member[],
  // 表单数据
  formData: {} as MemberForm,
  // 地址表单数据
  addressFormData: {} as AddressForm,
  // 弹窗控制
  dialog: {
    visible: false,
    title: "",
    type: "add",
  },
  // 地址弹窗控制
  addressDialog: {
    visible: false,
    title: "",
    type: "add",
  },
  // 地址列表
  addressList: [] as any[],
  // 当前选中的会员ID
  currentMemberId: "",
});

const { loading, queryParams, memberList, total } = toRefs(state);

function handleQuery() {
  state.loading = true;
  getMemberPage(state.queryParams).then(({ data }) => {
    state.memberList = data.list;
    state.total = data.total;
    state.loading = false;
  });
}

function resetQuery() {
  state.queryParams = {
    pageNum: 1,
    pageSize: 10,
    nickName: "",
  };
  handleQuery();
}

function handleSelectionChange(selection: any) {
  state.ids = selection.map((item: { id: any }) => item.id);
  state.single = selection.length != 1;
  state.multiple = !selection.length;
}

// 新增会员
function handleAdd() {
  state.dialog = {
    visible: true,
    title: "新增会员",
    type: "add",
  };
  state.formData = {
    nickName: "",
    gender: 0,
    mobile: "",
    birthday: "",
    avatarUrl: "",
    status: 1,
    balance: "0",
    point: 0,
    province: "",
    city: "",
    country: "",
    language: "",
  };
}

// 修改会员
function handleEdit(row: Member) {
  state.dialog = {
    visible: true,
    title: "修改会员",
    type: "edit",
  };
  state.formData = {
    id: row.id,
    nickName: row.nickName,
    gender: row.gender,
    mobile: row.mobile,
    birthday: row.birthday,
    avatarUrl: row.avatarUrl,
    status: row.status,
    balance: row.balance,
    point: row.point,
    province: row.province,
    city: row.city,
    country: row.country,
    language: row.language,
  };
}

// 删除会员
function handleDelete(row: Member) {
  ElMessageBox.confirm("确认删除该会员吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteMembers([row.id]).then(() => {
      ElMessage.success("删除成功");
      handleQuery();
    });
  });
}

// 批量删除
function handleBatchDelete() {
  if (state.ids.length === 0) {
    ElMessage.warning("请选择要删除的会员");
    return;
  }

  ElMessageBox.confirm("确认删除选中的会员吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteMembers(state.ids).then(() => {
      ElMessage.success("删除成功");
      handleQuery();
    });
  });
}

// 提交表单
function submitForm() {
  if (state.dialog.type === "add") {
    addMember(state.formData).then(() => {
      ElMessage.success("新增成功");
      state.dialog.visible = false;
      handleQuery();
    });
  } else {
    updateMember(state.formData.id!, state.formData).then(() => {
      ElMessage.success("修改成功");
      state.dialog.visible = false;
      handleQuery();
    });
  }
}

// 管理地址
function handleAddress(row: Member) {
  state.currentMemberId = row.id;
  state.addressDialog = {
    visible: true,
    title: `管理地址 - ${row.nickName}`,
    type: "manage",
  };
  loadAddresses(row.id);
}

// 加载地址列表
function loadAddresses(memberId: string) {
  getMemberAddresses(memberId).then(({ data }) => {
    state.addressList = data;
  });
}

// 新增地址
function handleAddAddress() {
  state.addressDialog = {
    visible: true,
    title: "新增地址",
    type: "add",
  };
  state.addressFormData = {
    memberId: state.currentMemberId,
    consigneeName: "",
    consigneeMobile: "",
    province: "",
    city: "",
    area: "",
    detailAddress: "",
    zipCode: "",
    defaulted: 0,
  };
}

// 修改地址
function handleEditAddress(row: any) {
  state.addressDialog = {
    visible: true,
    title: "修改地址",
    type: "edit",
  };
  state.addressFormData = { ...row };
}

// 删除地址
function handleDeleteAddress(row: any) {
  ElMessageBox.confirm("确认删除该地址吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteMemberAddresses([row.id]).then(() => {
      ElMessage.success("删除成功");
      loadAddresses(state.currentMemberId);
    });
  });
}

// 设置默认地址
function handleSetDefaultAddress(row: any) {
  setDefaultAddress(state.currentMemberId, row.id).then(() => {
    ElMessage.success("设置成功");
    loadAddresses(state.currentMemberId);
  });
}

// 提交地址表单
function submitAddressForm() {
  if (state.addressDialog.type === "add") {
    addMemberAddress(state.addressFormData).then(() => {
      ElMessage.success("新增成功");
      state.addressDialog.visible = false;
      loadAddresses(state.currentMemberId);
    });
  } else {
    updateMemberAddress(state.addressFormData.id!, state.addressFormData).then(
      () => {
        ElMessage.success("修改成功");
        state.addressDialog.visible = false;
        loadAddresses(state.currentMemberId);
      }
    );
  }
}

onMounted(() => {
  handleQuery();
});
</script>

<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item>
          <el-input
            v-model="queryParams.nickName"
            placeholder="会员昵称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="resetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="loading"
      :data="memberList"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" />
      <el-table-column type="expand" width="120" label="会员地址">
        <template #default="scope">
          <el-table :data="scope.row.addressList" size="small" border>
            <el-table-column
              type="index"
              label="序号"
              width="100"
              align="center"
            />
            <el-table-column align="center" label="收货人" prop="name" />
            <el-table-column align="center" label="联系方式" prop="mobile" />
            <el-table-column align="center" label="收货地址">
              <template #default>
                {{
                  scope.row.province +
                  scope.row.city +
                  scope.row.area +
                  scope.row.address
                }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="邮编" prop="zipCode" />
            <el-table-column align="center" label="是否默认">
              <template #default>
                <el-tag v-if="scope.row.defaulted == 1" type="success"
                  >是</el-tag
                >
                <el-tag v-if="scope.row.defaulted == 0" type="info">否</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>

      <el-table-column type="index" label="序号" width="100" align="center" />
      <el-table-column prop="nickName" label="昵称" />
      <el-table-column label="性别" width="80">
        <template #default="scope">
          <span v-if="scope.row.gender === 0">未知</span>
          <span v-if="scope.row.gender === 1">男</span>
          <span v-if="scope.row.gender === 2">女</span>
        </template>
      </el-table-column>
      <el-table-column label="头像" width="100">
        <template #default="scope">
          <el-popover placement="right" :width="400" trigger="hover">
            <img :src="scope.row.avatarUrl" width="400" height="400" />
            <template #reference>
              <img
                :src="scope.row.avatarUrl"
                style="max-width: 60px; max-height: 60px"
              />
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="mobile" label="手机号码" />
      <el-table-column prop="birthday" label="出生日期" />
      <el-table-column prop="status" width="80" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 1" type="success">正常</el-tag>
          <el-tag v-else type="info">禁用</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="注册时间" />

      <el-table-column label="账户余额">
        <template #default="scope">
          {{ scope.row.balance / 100 }}
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
  </div>
</template>
