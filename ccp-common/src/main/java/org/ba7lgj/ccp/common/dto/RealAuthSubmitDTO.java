package org.ba7lgj.ccp.common.dto;

import java.io.Serializable;

/**
 * 实名认证提交参数。
 */
public class RealAuthSubmitDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String idCardName;
    private String idCardNumber;
    private String faceImageUrl;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getIdCardName()
    {
        return idCardName;
    }

    public void setIdCardName(String idCardName)
    {
        this.idCardName = idCardName;
    }

    public String getIdCardNumber()
    {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber)
    {
        this.idCardNumber = idCardNumber;
    }

    public String getFaceImageUrl()
    {
        return faceImageUrl;
    }

    public void setFaceImageUrl(String faceImageUrl)
    {
        this.faceImageUrl = faceImageUrl;
    }
}
