<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item label="模块" prop="module">
          <el-input
            v-model="queryParams.module"
            placeholder="模块名"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item label="操作人" prop="username">
          <el-input
            v-model="queryParams.username"
            placeholder="操作人"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item label="操作时间" prop="createTime">
          <el-date-picker
            v-model="queryParams.createTime"
            type="daterange"
            range-separator="-"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD"
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><template #icon><i-ep-search /></template>搜索</el-button
          >
          <el-button @click="resetQuery">
            <template #icon><i-ep-refresh /></template>
            重置</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <el-table
        v-loading="loading"
        :data="logList"
        highlight-current-row
        border
      >
        <el-table-column label="编号" width="80" prop="id" />

        <el-table-column label="模块" width="120" prop="module" />

        <el-table-column label="操作人" width="120" prop="username" />

        <el-table-column label="操作" width="150" prop="operation" />

        <el-table-column label="方法" show-overflow-tooltip prop="method" />

        <el-table-column label="IP地址" width="150" prop="ip" />

        <el-table-column label="操作系统" width="100" prop="os" />

        <el-table-column label="浏览器" width="100" prop="browser" />

        <el-table-column label="结果" width="80" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.result === 'SUCCESS'" type="success"
              >成功</el-tag
            >
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作时间" width="180" prop="createTime" />

        <el-table-column label="操作" fixed="right" align="center" width="120">
          <template #default="scope">
            <el-button
              type="primary"
              link
              size="small"
              @click="openDetailDialog(scope.row)"
            >
              <i-ep-view />
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="handleQuery"
      />
    </el-card>

    <!-- 日志详情弹窗 -->
    <el-dialog
      v-model="detailDialog.visible"
      :title="detailDialog.title"
      width="60%"
      @close="closeDetailDialog"
      top="5vh"
    >
      <el-scrollbar max-height="70vh">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="模块">{{
            detailData.module
          }}</el-descriptions-item>
          <el-descriptions-item label="操作">{{
            detailData.operation
          }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{
            detailData.username
          }}</el-descriptions-item>
          <el-descriptions-item label="用户ID">{{
            detailData.userId
          }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{
            detailData.ip
          }}</el-descriptions-item>
          <el-descriptions-item label="操作系统">{{
            detailData.os
          }}</el-descriptions-item>
          <el-descriptions-item label="浏览器">{{
            detailData.browser
          }}</el-descriptions-item>
          <el-descriptions-item label="方法">{{
            detailData.method
          }}</el-descriptions-item>
          <el-descriptions-item label="URL">{{
            detailData.url
          }}</el-descriptions-item>
          <el-descriptions-item label="结果">
            <el-tag v-if="detailData.result === 'SUCCESS'" type="success"
              >成功</el-tag
            >
            <el-tag v-else type="danger">失败</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作时间">{{
            detailData.createTime
          }}</el-descriptions-item>

          <el-descriptions-item label="请求参数">
            <div class="content-wrapper">
              <pre>{{ detailData.params }}</pre>
            </div>
          </el-descriptions-item>

          <el-descriptions-item label="错误信息" v-if="detailData.errorMsg">
            <div class="content-wrapper error">
              <pre>{{ detailData.errorMsg }}</pre>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </el-scrollbar>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDetailDialog">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Log",
  inheritAttrs: false,
});

import { getLogPage } from "@/api/system/log";
import { LogQuery, LogVO } from "@/api/system/log/types";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<LogQuery>({
  pageNum: 1,
  pageSize: 10,
});

const logList = ref<LogVO[]>([]);

const detailDialog = reactive({
  title: "日志详情",
  visible: false,
});

const detailData = reactive<LogVO>({});

/**
 * 查询
 */
function handleQuery() {
  loading.value = true;
  getLogPage(queryParams)
    .then(({ data }) => {
      logList.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 重置查询 */
function resetQuery() {
  queryFormRef.value.resetFields();
  handleQuery();
}

/**
 * 打开详情弹窗
 *
 * @param data 日志数据
 */
function openDetailDialog(data: LogVO) {
  Object.assign(detailData, data);
  detailDialog.visible = true;
}

/** 关闭详情弹窗 */
function closeDetailDialog() {
  detailDialog.visible = false;
}

onMounted(() => {
  handleQuery();
});
</script>

<style scoped>
.content-wrapper {
  width: 100%;
  max-height: 200px;
  overflow-y: auto;
  padding: 12px;
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
  border: 1px solid var(--el-border-color-light);
}

.content-wrapper.error {
  background-color: var(--el-color-danger-light-9);
  border-color: var(--el-color-danger-light-5);
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  margin: 0;
  font-family: "Consolas", "Monaco", "Courier New", monospace;
  font-size: 12px;
  line-height: 1.5;
}

.dialog-footer {
  text-align: right;
  padding: 20px 0 0;
}
</style>
