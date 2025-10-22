<template>
  <div class="profile-container">
    <!-- 顶部用户信息卡片 -->
    <el-card class="user-profile-card">
      <div class="user-profile-header">
        <div class="user-avatar-section">
          <div class="avatar-container">
            <el-upload
              class="avatar-uploader"
              :action="uploadAction"
              :show-file-list="false"
              :headers="uploadHeaders"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarSuccess"
            >
              <div class="avatar-wrapper">
                <img
                  v-if="user.avatar"
                  :src="user.avatar + '?imageView2/1/w/120/h/120'"
                  class="avatar"
                />
                <div v-else class="avatar-placeholder">
                  <el-icon><User /></el-icon>
                </div>
                <div class="avatar-overlay">
                  <el-icon><Camera /></el-icon>
                </div>
              </div>
            </el-upload>
          </div>
        </div>
        <div class="user-info-section">
          <div class="user-main-info">
            <h1 class="username">{{ user.username }}</h1>
            <div class="user-meta">
              <div class="user-roles">
                <el-tag
                  v-for="role in user.roles"
                  :key="role"
                  class="role-tag"
                  size="small"
                >
                  {{ role }}
                </el-tag>
              </div>
              <el-tag
                :type="user.status === 1 ? 'success' : 'danger'"
                class="status-tag"
                size="small"
              >
                {{ user.status === 1 ? "正常" : "禁用" }}
              </el-tag>
            </div>
          </div>
          <div class="user-actions">
            <el-button type="primary" @click="resetPassword" size="small">
              <el-icon><Lock /></el-icon>
              重置密码
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 内容区域 -->
    <div class="content-section">
      <el-row :gutter="20">
        <!-- 左侧统计信息 -->
        <el-col :xs="24" :sm="12" :md="8">
          <el-card
            class="stats-card"
            shadow="hover"
            :body-style="{ height: '100%' }"
          >
            <template #header>
              <div class="card-header">
                <el-icon><DataAnalysis /></el-icon>
                <span>消息统计</span>
              </div>
            </template>
            <div class="stats-content">
              <div
                class="stat-item"
                v-for="stat in statItems"
                :key="stat.label"
              >
                <div class="stat-icon" :class="stat.bgColor">
                  <svg-icon :icon-class="stat.icon" />
                </div>
                <div class="stat-info">
                  <div class="stat-number">{{ stats[stat.key] }}</div>
                  <div class="stat-label">{{ stat.label }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧详细信息 -->
        <el-col :xs="24" :sm="12" :md="16">
          <el-card class="detail-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon><UserFilled /></el-icon>
                <span>详细信息</span>
              </div>
            </template>
            <div class="detail-content">
              <el-descriptions :column="1" border>
                <el-descriptions-item
                  v-for="field in detailFields"
                  :key="field.label"
                  :label="field.label"
                >
                  <div class="field-wrapper">
                    <span>{{ field.value || "未设置" }}</span>
                    <el-button
                      link
                      type="primary"
                      size="small"
                      @click="field.action"
                    >
                      {{ field.buttonText }}
                    </el-button>
                  </div>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 昵称性别修改弹窗 -->
    <el-dialog
      v-model="nicknameDialogVisible"
      title="修改昵称"
      width="500px"
      append-to-body
    >
      <el-form
        ref="nicknameFormRef"
        :model="nicknameForm"
        :rules="nicknameRules"
        label-width="80px"
      >
        <el-form-item label="用户昵称" prop="nickname">
          <el-input v-model="nicknameForm.nickname" maxlength="30" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="nicknameForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="nicknameDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitNicknameForm">
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 手机号修改弹窗 -->
    <el-dialog
      v-model="mobileDialogVisible"
      :title="user.mobile ? '更换手机号' : '绑定手机号'"
      width="500px"
      append-to-body
    >
      <el-form
        ref="mobileFormRef"
        :model="mobileForm"
        :rules="mobileRules"
        label-width="100px"
      >
        <el-form-item v-if="user.mobile" label="原手机号">
          {{ user.mobile }}
        </el-form-item>
        <el-form-item label="新手机号" prop="newMobile">
          <el-input v-model="mobileForm.newMobile" maxlength="11" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="mobileDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitMobileForm">
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 邮箱修改弹窗 -->
    <el-dialog
      v-model="emailDialogVisible"
      :title="user.email ? '更换邮箱' : '绑定邮箱'"
      width="500px"
      append-to-body
    >
      <el-form
        ref="emailFormRef"
        :model="emailForm"
        :rules="emailRules"
        label-width="100px"
      >
        <el-form-item v-if="user.email" label="原邮箱">
          {{ user.email }}
        </el-form-item>
        <el-form-item label="新邮箱" prop="newEmail">
          <el-input v-model="emailForm.newEmail" maxlength="50" />
        </el-form-item>
        <el-form-item label="验证码" prop="emailCode">
          <el-row :gutter="10">
            <el-col :span="14">
              <el-input
                v-model="emailForm.emailCode"
                placeholder="请输入验证码"
              />
            </el-col>
            <el-col :span="10">
              <el-button @click="sendEmailCode" :disabled="emailCodeTimer > 0">
                {{
                  emailCodeTimer > 0
                    ? `${emailCodeTimer}秒后重发`
                    : "发送验证码"
                }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="emailDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitEmailForm"> 确 定 </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 重置密码弹窗 -->
    <el-dialog
      v-model="passwordDialogVisible"
      title="重置密码"
      width="500px"
      append-to-body
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="80px"
      >
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitPasswordForm">
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  getUserForm,
  updateUser,
  updateUserPassword,
  updateSelfPassword,
  sendEmailCodeApi,
  validateEmailCodeApi,
} from "@/api/system/user";
import { useUserStore } from "@/store/modules/user";
import { UserForm } from "@/api/system/user/types";
import { UploadRawFile } from "element-plus";
import {
  User,
  Camera,
  Lock,
  DataAnalysis,
  UserFilled,
} from "@element-plus/icons-vue";

defineOptions({
  name: "Profile",
  inheritAttrs: false,
});

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

const nicknameFormRef = ref(ElForm);
const mobileFormRef = ref(ElForm);
const emailFormRef = ref(ElForm);
const passwordFormRef = ref(ElForm);

const user = computed(() => userStore.user);

type StatKey = "todos" | "messages" | "notifications";

// 统计项目配置
const statItems = [
  { label: "待办", key: "todos" as StatKey, icon: "todo", bgColor: "bg-blue" },
  {
    label: "消息",
    key: "messages" as StatKey,
    icon: "message",
    bgColor: "bg-green",
  },
  {
    label: "通知",
    key: "notifications" as StatKey,
    icon: "notification",
    bgColor: "bg-orange",
  },
];

// 详情字段配置
const detailFields = computed(() => [
  {
    label: "用户昵称",
    value: user.value.nickname,
    action: openNicknameDialog,
    buttonText: "修改",
  },
  {
    label: "性别",
    value:
      user.value.gender == 1 ? "男" : user.value.gender == 2 ? "女" : "未知",
    action: openNicknameDialog,
    buttonText: "修改",
  },
  {
    label: "手机号码",
    value: user.value.mobile,
    action: openMobileDialog,
    buttonText: user.value.mobile ? "更换" : "绑定",
  },
  {
    label: "用户邮箱",
    value: user.value.email,
    action: openEmailDialog,
    buttonText: user.value.email ? "更换" : "绑定",
  },
]);

const state = reactive({
  nicknameForm: {
    nickname: "",
    gender: 1,
  },
  mobileForm: {
    newMobile: "",
  },
  emailForm: {
    newEmail: "",
    emailCode: "",
  },
  passwordForm: {
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  },
  nicknameDialogVisible: false,
  mobileDialogVisible: false,
  emailDialogVisible: false,
  passwordDialogVisible: false,
  mobileCodeTimer: 0,
  emailCodeTimer: 0,
  stats: {
    todos: 12,
    messages: 3,
    notifications: 5,
  },
  uploadHeaders: {
    Authorization: localStorage.getItem("accessToken") || "",
  },
  nicknameRules: {
    nickname: [
      { required: true, message: "用户昵称不能为空", trigger: "blur" },
    ],
    gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  },
  mobileRules: {
    newMobile: [
      {
        required: true,
        pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        message: "请输入正确的手机号码",
        trigger: "blur",
      },
    ],
    mobileCode: [{ required: true, message: "请输入验证码", trigger: "blur" }],
  },
  emailRules: {
    newEmail: [
      {
        required: true,
        pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,
        message: "请输入正确的邮箱地址",
        trigger: "blur",
      },
    ],
    emailCode: [{ required: true, message: "请输入验证码", trigger: "blur" }],
  },
  passwordRules: {
    oldPassword: [
      { required: true, message: "旧密码不能为空", trigger: "blur" },
    ],
    newPassword: [
      { required: true, message: "新密码不能为空", trigger: "blur" },
      {
        pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[\s\S]{8,30}$/,
        message: "密码必须包含大小写字母和数字，长度在8-30之间",
        trigger: "blur",
      },
    ],
    confirmPassword: [
      { required: true, message: "确认密码不能为空", trigger: "blur" },
      {
        validator: (rule: any, value: string, callback: any) => {
          if (value !== state.passwordForm.newPassword) {
            callback(new Error("两次输入的密码不一致"));
          } else {
            callback();
          }
        },
        trigger: "blur",
      },
    ],
  },
});

const {
  nicknameForm,
  mobileForm,
  emailForm,
  passwordForm,
  nicknameDialogVisible,
  mobileDialogVisible,
  emailDialogVisible,
  passwordDialogVisible,
  emailCodeTimer,
  stats,
  uploadHeaders,
  nicknameRules,
  mobileRules,
  emailRules,
  passwordRules,
} = toRefs(state);

const uploadAction = computed(() => {
  return import.meta.env.VITE_APP_BASE_API + "/api/files/" + user.value.userId;
});

/**
 * 打开昵称修改弹窗
 */
function openNicknameDialog() {
  nicknameForm.value.nickname = user.value.nickname || "";
  nicknameForm.value.gender = user.value.gender || 1;
  nicknameDialogVisible.value = true;
}

/**
 * 提交昵称性别修改
 */
function submitNicknameForm() {
  if (!nicknameFormRef.value) return;
  nicknameFormRef.value.validate((valid: boolean) => {
    if (valid) {
      const data = {
        nickname: nicknameForm.value.nickname,
        gender: nicknameForm.value.gender,
      };
      updateUser(user.value.userId as number, data).then(() => {
        ElMessage.success("修改成功");
        nicknameDialogVisible.value = false;
        // 更新用户信息
        userStore.getUserInfo();
      });
    }
  });
}

/**
 * 打开手机号修改弹窗
 */
function openMobileDialog() {
  mobileForm.value.newMobile = "";
  mobileDialogVisible.value = true;
}

/**
 * 提交手机号修改
 */
function submitMobileForm() {
  if (!mobileFormRef.value) return;
  mobileFormRef.value.validate((valid: boolean) => {
    if (valid) {
      const data = {
        mobile: mobileForm.value.newMobile,
      };
      updateUser(user.value.userId as number, data).then(() => {
        ElMessage.success("手机号修改成功");
        mobileDialogVisible.value = false;
        // 更新用户信息
        userStore.getUserInfo();
      });
    }
  });
}

/**
 * 打开邮箱修改弹窗
 */
function openEmailDialog() {
  emailForm.value.newEmail = "";
  emailForm.value.emailCode = "";
  emailDialogVisible.value = true;
}

/**
 * 发送邮箱验证码
 */
function sendEmailCode() {
  if (!emailFormRef.value) return;
  emailFormRef.value.validateField("newEmail", (valid: boolean) => {
    if (valid) {
      // 调用发送验证码接口
      sendEmailCodeApi(emailForm.value.newEmail).then(() => {
        ElMessage.success("验证码已发送");
      });
      // 启动倒计时
      state.emailCodeTimer = 60;
      const timer = setInterval(() => {
        state.emailCodeTimer--;
        if (state.emailCodeTimer <= 0) {
          clearInterval(timer);
        }
      }, 1000);
    } else {
      ElMessage.error("请输入正确的邮箱地址");
    }
  });
}

/**
 * 提交邮箱修改
 */
function submitEmailForm() {
  if (!emailFormRef.value) return;
  emailFormRef.value.validate((valid: boolean) => {
    if (valid) {
      // 先验证验证码
      validateEmailCodeApi(emailForm.value.newEmail, emailForm.value.emailCode)
        .then(() => {
          // 验证成功后更新邮箱
          const data = {
            email: emailForm.value.newEmail,
          };
          updateUser(user.value.userId as number, data).then(() => {
            ElMessage.success("邮箱修改成功");
            emailDialogVisible.value = false;
            // 更新用户信息
            userStore.getUserInfo();
          });
        })
        .catch(() => {
          ElMessage.error("验证码错误或已失效");
        });
    }
  });
}

/**
 * 重置密码按钮
 */
function resetPassword() {
  passwordDialogVisible.value = true;
  passwordForm.value = {
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  };
}

/**
 * 提交修改密码
 */
function submitPasswordForm() {
  if (!passwordFormRef.value) return;
  passwordFormRef.value.validate((valid: boolean) => {
    if (valid) {
      updateSelfPassword(
        user.value.userId as number,
        passwordForm.value.oldPassword,
        passwordForm.value.newPassword
      )
        .then(() => {
          ElMessage.success("密码修改成功");
          passwordDialogVisible.value = false;
        })
        .catch((error) => {
          ElMessage.error(error.message || "密码修改失败");
        });
    }
  });
}

/**
 * 头像上传前处理
 */
const beforeAvatarUpload = (rawFile: UploadRawFile) => {
  if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png") {
    ElMessage.error("头像图片必须是 JPG 或 PNG 格式!");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("头像图片大小不能超过 2MB!");
    return false;
  }
  return true;
};

/**
 * 头像上传成功处理
 */
const handleAvatarSuccess = (response: any) => {
  ElMessage.success("头像更新成功");
  // 更新用户信息
  userStore.getUserInfo();
};

onMounted(() => {
  // 页面加载时不需要初始化用户表单数据，因为直接从userStore获取
});
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 84px);
}

// 用户信息卡片样式
.user-profile-card {
  margin-bottom: 20px;

  .user-profile-header {
    display: flex;
    align-items: center;
    padding: 24px;

    .user-avatar-section {
      margin-right: 24px;

      .avatar-container {
        .avatar-uploader {
          .avatar-wrapper {
            position: relative;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            overflow: hidden;
            cursor: pointer;
            border: 2px solid #f0f2f5;
            transition: all 0.3s ease;

            &:hover {
              border-color: var(--el-color-primary);

              .avatar-overlay {
                opacity: 1;
              }
            }

            .avatar {
              width: 100%;
              height: 100%;
              object-fit: cover;
            }

            .avatar-placeholder {
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
              background-color: #f5f7fa;
              color: #c0c4cc;

              .el-icon {
                font-size: 40px;
              }
            }

            .avatar-overlay {
              position: absolute;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background-color: rgba(0, 0, 0, 0.5);
              display: flex;
              align-items: center;
              justify-content: center;
              color: white;
              opacity: 0;
              transition: opacity 0.3s ease;

              .el-icon {
                font-size: 24px;
              }
            }
          }
        }
      }
    }

    .user-info-section {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .user-main-info {
        .username {
          font-size: 24px;
          font-weight: 600;
          margin: 0 0 12px 0;
          color: #1f2d3d;
        }

        .user-meta {
          display: flex;
          align-items: center;
          gap: 12px;

          .user-roles {
            display: flex;
            gap: 8px;
          }
        }
      }
    }
  }
}

// 内容区域
.content-section {
  .el-row {
    margin-left: -10px !important;
    margin-right: -10px !important;

    .el-col {
      padding-left: 10px !important;
      padding-right: 10px !important;
    }
  }
}

// 统计卡片
.stats-card {
  height: calc(100% - 20px);

  :deep(.el-card__body) {
    padding: 0;
    height: 100%;
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;

    .el-icon {
      color: var(--el-color-primary);
    }
  }

  .stats-content {
    padding: 20px;
    height: calc(100% - 56px);

    .stat-item {
      display: flex;
      align-items: center;
      padding: 16px 0;

      &:not(:last-child) {
        border-bottom: 1px solid #f0f2f5;
      }

      .stat-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        color: white;
        font-size: 20px;

        &.bg-blue {
          background: linear-gradient(135deg, #409eff, #3375b9);
        }

        &.bg-green {
          background: linear-gradient(135deg, #67c23a, #529b2e);
        }

        &.bg-orange {
          background: linear-gradient(135deg, #e6a23c, #b88226);
        }
      }

      .stat-info {
        .stat-number {
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 4px;
          color: #1f2d3d;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
}

// 详情卡片
.detail-card {
  height: calc(100% - 20px);

  :deep(.el-card__body) {
    padding: 20px;
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;

    .el-icon {
      color: var(--el-color-primary);
    }
  }

  .detail-content {
    .field-wrapper {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .profile-container {
    padding: 16px;
  }

  .user-profile-header {
    flex-direction: column;
    text-align: center;

    .user-avatar-section {
      margin-right: 0;
      margin-bottom: 20px;
    }

    .user-info-section {
      flex-direction: column;
      gap: 16px;
      width: 100%;

      .user-main-info {
        width: 100%;

        .user-meta {
          justify-content: center;
          flex-wrap: wrap;
        }
      }

      .user-actions {
        width: 100%;

        .el-button {
          width: 100%;
        }
      }
    }
  }

  .stats-card,
  .detail-card {
    height: auto;
  }

  .stats-card {
    .stats-content {
      height: auto;
    }
  }
}
</style>
