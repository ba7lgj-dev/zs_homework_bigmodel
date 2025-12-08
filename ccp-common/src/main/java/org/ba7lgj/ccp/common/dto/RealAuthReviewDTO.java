package org.ba7lgj.ccp.common.dto;

import java.io.Serializable;

/**
 * 实名认证审核参数。
 */
public class RealAuthReviewDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Integer targetStatus;
    private String failReason;
    private Long reviewBy;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getTargetStatus()
    {
        return targetStatus;
    }

    public void setTargetStatus(Integer targetStatus)
    {
        this.targetStatus = targetStatus;
    }

    public String getFailReason()
    {
        return failReason;
    }

    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }

    public Long getReviewBy()
    {
        return reviewBy;
    }

    public void setReviewBy(Long reviewBy)
    {
        this.reviewBy = reviewBy;
    }
}
