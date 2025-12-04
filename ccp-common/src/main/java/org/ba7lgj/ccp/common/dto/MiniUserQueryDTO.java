package org.ba7lgj.ccp.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 小程序用户查询参数。
 */
public class MiniUserQueryDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String nickName;
    private String phone;
    private String studentNo;
    private Integer gender;
    private Integer status;
    private Integer authStatus;
    private Date beginTime;
    private Date endTime;

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
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

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
}
