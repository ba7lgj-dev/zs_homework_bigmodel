package com.ruoyi.ccp.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小程序用户对象 mini_user
 *
 * @author ruoyi
 */
public class MiniUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 微信 openid */
    @Excel(name = "微信openid")
    private String openId;

    /** 微信 unionid */
    @Excel(name = "微信unionid")
    private String unionId;

    /** 微信昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 微信头像 */
    @Excel(name = "头像地址")
    private String avatarUrl;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNo;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 性别 0未知 1男 2女 */
    @Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
    private Integer gender;

    /** 账号状态：1正常 0封禁 */
    @Excel(name = "账号状态", readConverterExp = "0=封禁,1=正常")
    private Integer status;

    /** 认证状态：0未认证 1待审核 2审核通过 3审核不通过 */
    @Excel(name = "认证状态", readConverterExp = "0=未认证,1=待审核,2=审核通过,3=审核不通过")
    private Integer authStatus;

    /** 审核失败原因 */
    @Excel(name = "审核失败原因")
    private String authFailReason;

    /** 审核管理员ID */
    @Excel(name = "审核人")
    private Long reviewBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 管理员备注 */
    @Excel(name = "管理员备注")
    private String adminRemark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 性别描述 */
    private transient String genderName;

    /** 状态描述 */
    private transient String statusName;

    /** 认证状态描述 */
    private transient String authStatusName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getUnionId()
    {
        return unionId;
    }

    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getAuthStatus()
    {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus)
    {
        this.authStatus = authStatus;
    }

    public String getAuthFailReason()
    {
        return authFailReason;
    }

    public void setAuthFailReason(String authFailReason)
    {
        this.authFailReason = authFailReason;
    }

    public Long getReviewBy()
    {
        return reviewBy;
    }

    public void setReviewBy(Long reviewBy)
    {
        this.reviewBy = reviewBy;
    }

    public Date getReviewTime()
    {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime)
    {
        this.reviewTime = reviewTime;
    }

    public String getAdminRemark()
    {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark)
    {
        this.adminRemark = adminRemark;
    }

    @Override
    public Date getCreateTime()
    {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime()
    {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getGenderName()
    {
        if (gender == null)
        {
            return null;
        }
        switch (gender)
        {
            case 1:
                return "男";
            case 2:
                return "女";
            default:
                return "未知";
        }
    }

    public void setGenderName(String genderName)
    {
        this.genderName = genderName;
    }

    public String getStatusName()
    {
        if (status == null)
        {
            return null;
        }
        return status == 1 ? "正常" : "禁用";
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    public String getAuthStatusName()
    {
        if (authStatus == null)
        {
            return null;
        }
        switch (authStatus)
        {
            case 1:
                return "待审核";
            case 2:
                return "审核通过";
            case 3:
                return "审核不通过";
            default:
                return "未认证";
        }
    }

    public void setAuthStatusName(String authStatusName)
    {
        this.authStatusName = authStatusName;
    }
}
