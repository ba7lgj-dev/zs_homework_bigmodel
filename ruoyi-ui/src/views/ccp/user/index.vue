<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="昵称" prop="nick_name">
        <el-input v-model="queryParams.nick_name" placeholder="输入昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="手机号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号" prop="student_no">
        <el-input v-model="queryParams.student_no" placeholder="学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="认证状态" prop="auth_status">
        <el-select v-model="queryParams.auth_status" placeholder="请选择" clearable>
          <el-option v-for="dict in authStatusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="封禁" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['ccp:user:add']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['ccp:user:export']" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList" border>
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="昵称" prop="nickName" />
      <el-table-column label="头像" width="120">
        <template slot-scope="scope">
          <el-image v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" style="height:40px" fit="contain" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" prop="phone" />
      <el-table-column label="学号" prop="studentNo" />
      <el-table-column label="真实姓名" prop="realName" />
      <el-table-column label="性别">
        <template slot-scope="scope">
          <dict-tag :options="genderOptions" :value="scope.row.gender" />
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '正常' : '封禁' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="认证状态">
        <template slot-scope="scope">
          <dict-tag :options="authStatusOptions" :value="scope.row.authStatus" />
        </template>
      </el-table-column>
      <el-table-column label="审核时间" prop="reviewTime" width="160" />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" width="320" fixed="right">
        <template slot-scope="scope">
          <el-button v-hasPermi="['ccp:user:review']" size="mini" type="text" v-if="scope.row.authStatus === 1" @click="openAudit(scope.row)">审核</el-button>
          <el-button v-hasPermi="['ccp:user:changeStatus']" size="mini" type="text" v-if="scope.row.status === 1" @click="handleStatus(scope.row,0)">禁用</el-button>
          <el-button v-hasPermi="['ccp:user:changeStatus']" size="mini" type="text" v-if="scope.row.status === 0" @click="handleStatus(scope.row,1)">启用</el-button>
          <el-button v-hasPermi="['ccp:user:edit']" size="mini" type="text" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-hasPermi="['ccp:user:remove']" size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="auditTitle" :visible.sync="auditOpen" width="500px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="auditForm.realName" disabled />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="auditForm.studentNo" disabled />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="auditForm.phone" disabled />
        </el-form-item>
        <el-form-item label="审核结果">
          <el-select v-model="auditForm.targetAuthStatus" placeholder="请选择">
            <el-option v-for="dict in authStatusOptions.filter(item => item.dictValue == 2 || item.dictValue == 3)" :key="dict.dictValue" :label="dict.dictLabel" :value="Number(dict.dictValue)" />
          </el-select>
        </el-form-item>
        <el-form-item label="失败原因" v-if="auditForm.targetAuthStatus === 3">
          <el-input type="textarea" v-model="auditForm.authFailReason" placeholder="请输入原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitAudit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" label-width="80px">
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
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择">
            <el-option v-for="dict in genderOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="Number(dict.dictValue)" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="adminRemark">
          <el-input type="textarea" v-model="form.adminRemark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, delUser, delUserBatch, changeUserStatus, reviewUser, exportUser } from '@/api/ccp/user'
import { getDicts } from '@/api/system/dict/data'
import { download } from '@/utils/request'

export default {
  name: 'CcpUser',
  data() {
    return {
      loading: false,
      total: 0,
      userList: [],
      title: '编辑用户',
      open: false,
      auditOpen: false,
      auditTitle: '审核用户',
      genderOptions: [],
      authStatusOptions: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nick_name: undefined,
        phone: undefined,
        student_no: undefined,
        auth_status: undefined,
        status: undefined
      },
      form: {},
      auditForm: {}
    }
  },
  created() {
    this.getDictsData()
    this.getList()
  },
  methods: {
    getDictsData() {
      getDicts('ccp_user_gender').then(res => {
        this.genderOptions = res.data
      })
      getDicts('ccp_auth_status').then(res => {
        this.authStatusOptions = res.data
      })
    },
    getList() {
      this.loading = true
      const params = Object.assign({}, this.queryParams)
      if (this.dateRange && this.dateRange.length === 2) {
        params.beginTime = this.dateRange[0]
        params.endTime = this.dateRange[1]
      }
      listUser(params).then(response => {
        this.userList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams = {
        pageNum: 1,
        pageSize: 10
      }
      this.getDictsData()
      this.handleQuery()
    },
    handleAdd() {
      this.form = { gender: 0, status: 1, authStatus: 0 }
      this.open = true
    },
    handleUpdate(row) {
      getUser(row.id).then(res => {
        this.form = res.data
        this.open = true
      })
    },
    handleDelete(row) {
      this.$modal.confirm(`确认删除编号为 ${row.id} 的用户吗?`).then(() => {
        return delUser(row.id)
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      })
    },
    handleStatus(row, status) {
      const data = { id: row.id, status: status }
      changeUserStatus(data).then(() => {
        this.$modal.msgSuccess('状态更新成功')
        this.getList()
      })
    },
    openAudit(row) {
      this.auditForm = {
        id: row.id,
        phone: row.phone,
        studentNo: row.studentNo,
        realName: row.realName,
        targetAuthStatus: 2,
        authFailReason: ''
      }
      this.auditOpen = true
    },
    submitAudit() {
      if (this.auditForm.targetAuthStatus === 3 && !this.auditForm.authFailReason) {
        this.$modal.msgWarning('请填写失败原因')
        return
      }
      reviewUser(this.auditForm).then(() => {
        this.$modal.msgSuccess('审核成功')
        this.auditOpen = false
        this.getList()
      })
    },
    submitForm() {
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
    },
    handleExport() {
      exportUser(this.queryParams).then(response => {
        download(response, `ccp_user_${new Date().getTime()}.xlsx`)
      })
    }
  }
}
</script>
