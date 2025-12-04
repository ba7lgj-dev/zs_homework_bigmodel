<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="queryParams.nickName" placeholder="请输入昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="认证状态" prop="authStatus">
        <el-select v-model="queryParams.authStatus" placeholder="请选择认证状态" clearable>
          <el-option v-for="item in authStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:user:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading" @click="handleExport" v-hasPermi="['ccp:user:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="头像" align="center" width="80">
        <template slot-scope="scope">
          <el-image v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" :preview-src-list="[scope.row.avatarUrl]" style="width:40px;height:40px;border-radius:50%" />
        </template>
      </el-table-column>
      <el-table-column label="昵称" align="center" prop="nickName" />
      <el-table-column label="手机号" align="center" prop="phone" width="120" />
      <el-table-column label="学号" align="center" prop="studentNo" width="120" />
      <el-table-column label="真实姓名" align="center" prop="realName" width="120" />
      <el-table-column label="性别" align="center" prop="gender">
        <template slot-scope="scope">
          <span>{{ genderName(scope.row.gender) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="账号状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '正常' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="认证状态" align="center" prop="authStatus" width="120">
        <template slot-scope="scope">
          <el-tag :type="authTagType(scope.row.authStatus)">{{ authStatusText(scope.row.authStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="reviewBy" width="120" />
      <el-table-column label="审核时间" align="center" prop="reviewTime" width="180" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button v-if="scope.row.authStatus === 1" size="mini" type="primary" plain icon="el-icon-success" @click="openReview(scope.row)" v-hasPermi="['ccp:user:review']">审核</el-button>
          <el-button v-if="scope.row.status === 1" size="mini" type="warning" plain icon="el-icon-remove" @click="handleStatus(scope.row, 0)" v-hasPermi="['ccp:user:changeStatus']">禁用</el-button>
          <el-button v-else size="mini" type="success" plain icon="el-icon-check" @click="handleStatus(scope.row, 1)" v-hasPermi="['ccp:user:changeStatus']">启用</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:user:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ccp:user:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="OpenId" prop="openId" v-if="!form.id">
          <el-input v-model="form.openId" placeholder="请输入openid" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="form.studentNo" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="备注" prop="adminRemark">
          <el-input v-model="form.adminRemark" placeholder="请输入管理员备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="用户审核" :visible.sync="reviewOpen" width="500px" append-to-body>
      <el-form ref="reviewFormRef" :model="reviewForm" label-width="100px">
        <el-form-item label="认证结果">
          <el-radio-group v-model="reviewForm.authStatus">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="失败原因" v-if="reviewForm.authStatus === 3">
          <el-input type="textarea" v-model="reviewForm.authFailReason" placeholder="请输入失败原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReview">确 定</el-button>
        <el-button @click="reviewOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, changeUserStatus, reviewUser, exportUser } from '@/api/ccp/user'

export default {
  name: 'CcpUser',
  data() {
    return {
      loading: false,
      exportLoading: false,
      showSearch: true,
      total: 0,
      userList: [],
      dateRange: [],
      ids: [],
      single: true,
      multiple: true,
      title: '',
      open: false,
      reviewOpen: false,
      form: {},
      reviewForm: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phone: undefined,
        studentNo: undefined,
        authStatus: undefined,
        status: undefined
      },
      authStatusOptions: [
        { label: '未认证', value: 0 },
        { label: '待审核', value: 1 },
        { label: '已通过', value: 2 },
        { label: '未通过', value: 3 }
      ],
      statusOptions: [
        { label: '正常', value: 1 },
        { label: '禁用', value: 0 }
      ],
      rules: {
        openId: [{ required: true, message: 'openid不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    genderName(gender) {
      if (gender === 1) return '男'
      if (gender === 2) return '女'
      return '未知'
    },
    authStatusText(status) {
      const map = { 0: '未认证', 1: '待审核', 2: '已通过', 3: '未通过' }
      return map[status] || '未认证'
    },
    authTagType(status) {
      if (status === 1) return 'warning'
      if (status === 2) return 'success'
      if (status === 3) return 'danger'
      return 'info'
    },
    getList() {
      this.loading = true
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(res => {
        this.userList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    reset() {
      this.form = {
        id: undefined,
        openId: undefined,
        nickName: undefined,
        phone: undefined,
        studentNo: undefined,
        realName: undefined,
        adminRemark: undefined
      }
      this.resetForm('form')
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.title = '新增用户'
      this.open = true
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getUser(id).then(res => {
        this.form = res.data
        this.title = '修改用户'
        this.open = true
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) return
        if (this.form.id) {
          updateUser(this.form).then(() => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
        } else {
          addUser(this.form).then(() => {
            this.$modal.msgSuccess('新增成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除选中的用户？').then(function() {
        return delUser(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleStatus(row, status) {
      const text = status === 1 ? '启用' : '禁用'
      this.$modal.confirm(`确认要${text}用户【${row.nickName || row.id}】吗？`).then(() => {
        return changeUserStatus(row.id, status)
      }).then(() => {
        this.$modal.msgSuccess('操作成功')
        this.getList()
      }).catch(() => {})
    },
    openReview(row) {
      this.reviewForm = {
        id: row.id,
        authStatus: 2,
        authFailReason: ''
      }
      this.reviewOpen = true
    },
    submitReview() {
      if (this.reviewForm.authStatus === 3 && !this.reviewForm.authFailReason) {
        this.$modal.msgError('请填写失败原因')
        return
      }
      reviewUser(this.reviewForm).then(() => {
        this.$modal.msgSuccess('审核成功')
        this.reviewOpen = false
        this.getList()
      })
    },
    handleExport() {
      this.exportLoading = true
      this.download('/ccp/user/export', this.addDateRange(this.queryParams, this.dateRange), `mini_user_${new Date().getTime()}.xlsx`)
        .finally(() => {
          this.exportLoading = false
        })
    }
  }
}
</script>
