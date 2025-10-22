<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form
        ref="queryFormRef"
        :model="queryParams"
        :inline="true"
        label-suffix=":"
      >
        <el-form-item label="标题123" prop="title">
          <el-input
            v-model="queryParams.title"
            placeholder="标题"
            clearable
            @keyup.enter="handleQuery()"
          />
        </el-form-item>

        <el-form-item label="发布状态" prop="status">
          <el-select
            v-model="queryParams.status"
            clearable
            placeholder="全部"
            style="width: 100px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
          </el-select>
        </el-form-item>

        <el-form-item class="search-buttons">
          <el-button type="primary" icon="search" @click="handleQuery()"
            >搜索</el-button
          >
          <el-button icon="refresh" @click="handleResetQuery()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="hover" class="data-table">
      <div class="data-table__toolbar">
        <div class="data-table__toolbar--actions">
          <el-button
            v-hasPerm="['sys:notice:add']"
            type="success"
            icon="plus"
            @click="handleOpenDialog()"
          >
            新增通知
          </el-button>
          <el-button
            v-hasPerm="['sys:notice:del']"
            type="danger"
            :disabled="selectIds.length === 0"
            icon="delete"
            @click="handleDelete()"
          >
            删除
          </el-button>
        </div>
      </div>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="pageData"
        highlight-current-row
        class="data-table__content"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column label="通知标题" prop="title" min-width="200" />
        <el-table-column align="center" label="通知类型" width="150">
          <template #default="scope">
            <el-tag v-if="scope.row.type === '1'">通知</el-tag>
            <el-tag v-else-if="scope.row.type === '2'" type="success"
              >公告</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="发布人"
          prop="publisher"
          width="150"
        />
        <el-table-column
          align="center"
          label="发布人角色"
          prop="publisherRole"
          width="150"
        />
        <el-table-column align="center" label="通知等级" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.level === 'HIGH'" type="danger">高</el-tag>
            <el-tag v-else-if="scope.row.level === 'MEDIUM'" type="warning"
              >中</el-tag
            >
            <el-tag v-else-if="scope.row.level === 'LOW'" type="info"
              >低</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="目标角色"
          prop="targetRoles"
          width="150"
        />
        <el-table-column
          align="center"
          label="目标用户"
          prop="targetUsers"
          width="150"
        />
        <el-table-column align="center" label="发布状态" min-width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 'DRAFT'" type="info"
              >草稿</el-tag
            >
            <el-tag v-else-if="scope.row.status === 'PUBLISHED'" type="success"
              >已发布</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作时间" width="250">
          <template #default="scope">
            <div class="flex-x-start">
              <span>创建时间：</span>
              <span>{{ scope.row.createTime || "-" }}</span>
            </div>

            <div v-if="scope.row.status === 'PUBLISHED'" class="flex-x-start">
              <span>发布时间：</span>
              <span>{{ scope.row.publishTime || "-" }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建者"
          prop="createBy"
          width="150"
        />
        <el-table-column
          align="center"
          label="更新时间"
          prop="updateTime"
          width="150"
        />
        <el-table-column
          align="center"
          label="备注"
          prop="remark"
          width="150"
        />
        <el-table-column align="center" fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="openDetailDialog(scope.row.noticeId)"
            >
              查看
            </el-button>
            <el-button
              v-if="scope.row.status !== 'PUBLISHED'"
              v-hasPerm="['sys:notice:publish']"
              type="primary"
              size="small"
              link
              @click="handlePublish(scope.row.noticeId)"
            >
              发布
            </el-button>
            <el-button
              v-if="scope.row.status === 'PUBLISHED'"
              v-hasPerm="['sys:notice:revoke']"
              type="primary"
              size="small"
              link
              @click="handleRevoke(scope.row.noticeId)"
            >
              撤回
            </el-button>
            <el-button
              v-if="scope.row.status !== 'PUBLISHED'"
              v-hasPerm="['sys:notice:edit']"
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row.noticeId)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.status !== 'PUBLISHED'"
              v-hasPerm="['sys:notice:del']"
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row.noticeId)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="fetchData()"
      />
    </el-card>

    <!-- 通知公告表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      top="3vh"
      width="80%"
      @close="handleCloseDialog"
    >
      <el-form
        ref="dataFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="通知标题" prop="title">
              <el-input
                v-model="formData.title"
                placeholder="通知标题"
                clearable
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="通知类型" prop="type">
              <Dictionary v-model="formData.type" type-code="notice_type" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="通知等级" prop="level">
              <Dictionary v-model="formData.level" type-code="notice_level" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="发布人" prop="publisher">
              <el-input
                v-model="formData.publisher"
                placeholder="发布人"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目标角色" prop="targetRoles">
              <el-select
                v-model="formData.targetRoles"
                multiple
                placeholder="请选择目标角色"
                style="width: 100%"
              >
                <el-option label="管理员" value="ADMIN" />
                <el-option label="普通用户" value="USER" />
                <el-option label="访客" value="GUEST" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="目标用户" prop="targetUsers">
              <el-input
                v-model="targetUsersString"
                placeholder="目标用户ID，多个用逗号分隔"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            placeholder="备注信息"
            :rows="2"
            clearable
          />
        </el-form-item>

        <el-form-item label="通知内容" prop="content">
          <WangEditor
            v-model="formData.content"
            style="min-height: 400px; max-height: 600px; overflow-y: auto"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit()">确定</el-button>
          <el-button @click="handleCloseDialog()">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 通知公告详情 -->
    <el-dialog
      v-model="detailDialog.visible"
      :show-close="false"
      width="50%"
      append-to-body
      @close="closeDetailDialog"
    >
      <template #header>
        <div class="flex-x-between">
          <span>通知公告详情</span>
          <div class="dialog-toolbar">
            <el-button circle @click="closeDetailDialog">
              <template #icon>
                <Close />
              </template>
            </el-button>
          </div>
        </div>
      </template>
      <el-descriptions :column="1">
        <el-descriptions-item label="标题：">
          {{ currentNotice.title }}
        </el-descriptions-item>
        <el-descriptions-item label="发布状态：">
          <el-tag v-if="currentNotice.status === 'DRAFT'" type="info"
            >草稿</el-tag
          >
          <el-tag
            v-else-if="currentNotice.status === 'PUBLISHED'"
            type="success"
            >已发布</el-tag
          >
        </el-descriptions-item>
        <el-descriptions-item label="发布人：">
          {{ currentNotice.publisher }}
        </el-descriptions-item>
        <el-descriptions-item label="发布时间：">
          {{ currentNotice.publishTime }}
        </el-descriptions-item>
        <el-descriptions-item label="公告内容：">
          <div class="notice-content" v-html="currentNotice.content"></div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Notice",
  inheritAttrs: false,
});

import * as NoticeAPI from "@/api/system/notice";
import type {
  NoticePageVO,
  NoticeForm,
  NoticeQuery as NoticePageQuery,
  NoticeDetailVO,
} from "@/api/system/notice/types";

const queryFormRef = ref();
const dataFormRef = ref();

const loading = ref(false);
const selectIds = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<NoticePageQuery>({
  pageNum: 1,
  pageSize: 10,
});

const userOptions = ref<OptionType[]>([]);
// 通知公告表格数据
const pageData = ref<NoticePageVO[]>([]);

// 弹窗
const dialog = reactive({
  title: "",
  visible: false,
});

// 通知公告表单数据
const formData = reactive<NoticeForm>({
  level: "LOW", // 默认优先级为低
  title: "",
  type: "",
  publisher: "",
} as NoticeForm);

// 将 targetUsers 数组转换为字符串用于表单输入
const targetUsersString = computed({
  get: () => {
    if (Array.isArray(formData.targetUsers)) {
      return formData.targetUsers.join(",");
    }
    return formData.targetUsers || "";
  },
  set: (val: string) => {
    if (val) {
      formData.targetUsers = val
        .split(",")
        .map((item) => item.trim())
        .filter((item) => item);
    } else {
      formData.targetUsers = [];
    }
  },
});

// 通知公告表单校验规则
const rules = reactive({
  title: [{ required: true, message: "请输入通知标题", trigger: "blur" }],
  content: [
    {
      required: true,
      message: "请输入通知内容",
      trigger: "blur",
      validator: (rule: any, value: string, callback: any) => {
        if (!value || !value.replace(/<[^>]+>/g, "").trim()) {
          callback(new Error("请输入通知内容"));
        } else {
          callback();
        }
      },
    },
  ],
  type: [{ required: true, message: "请选择通知类型", trigger: "change" }],
  publisher: [{ required: true, message: "请输入发布人", trigger: "blur" }],
});

const detailDialog = reactive({
  visible: false,
});
const currentNotice = ref<NoticeDetailVO>({
  id: "",
  title: "",
  content: "",
  publisherName: "",
  publishTime: "",
});

// 查询通知公告
function handleQuery() {
  queryParams.pageNum = 1;
  fetchData();
}

//发送请求接口
function fetchData() {
  loading.value = true;
  NoticeAPI.getNoticePage(queryParams)
    .then((res) => {
      pageData.value = res.data.list;
      total.value = res.data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

// 重置查询
function handleResetQuery() {
  queryFormRef.value!.resetFields();
  queryParams.pageNum = 1;
  handleQuery();
}

// 行复选框选中项变化
function handleSelectionChange(selection: any) {
  selectIds.value = selection.map((item: any) => item.noticeId);
}

// 打开通知公告弹窗
function handleOpenDialog(noticeId?: number) {
  dialog.visible = true;
  if (noticeId) {
    dialog.title = "修改公告";
    NoticeAPI.getNoticeForm(noticeId).then((data) => {
      Object.assign(formData, data);
    });
  } else {
    Object.assign(formData, {
      level: "LOW",
      title: "",
      type: "",
      publisher: "",
      targetRoles: [],
      targetUsers: [],
      remark: "",
    });
    dialog.title = "新增公告";
  }
}

// 发布通知公告
function handlePublish(noticeId: number) {
  NoticeAPI.publishNotice(noticeId).then(() => {
    ElMessage.success("发布成功");
    handleQuery();
  });
}

// 撤回通知公告
function handleRevoke(noticeId: number) {
  NoticeAPI.revokeNotice(noticeId).then(() => {
    ElMessage.success("撤回成功");
    handleQuery();
  });
}

// 通知公告表单提交
function handleSubmit() {
  dataFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const noticeId = formData.noticeId;
      if (noticeId) {
        NoticeAPI.updateNotice(noticeId, formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        NoticeAPI.addNotice(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

// 重置表单
function resetForm() {
  dataFormRef.value.resetFields();
  dataFormRef.value.clearValidate();
  formData.noticeId = undefined;
}

// 关闭通知公告弹窗
function handleCloseDialog() {
  dialog.visible = false;
  resetForm();
}

// 删除通知公告
function handleDelete(noticeId?: number) {
  const deleteIds = [noticeId || selectIds.value].join(",");
  if (!deleteIds) {
    ElMessage.warning("请勾选删除项");
    return;
  }

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      NoticeAPI.deleteNotices(deleteIds)
        .then(() => {
          ElMessage.success("删除成功");
          handleResetQuery();
        })
        .finally(() => (loading.value = false));
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
}

const closeDetailDialog = () => {
  detailDialog.visible = false;
};

const openDetailDialog = async (id: number) => {
  const { data } = await NoticeAPI.getDetail(id.toString());
  currentNotice.value = data;
  detailDialog.visible = true;
};

onMounted(() => {
  handleQuery();
});
</script>
