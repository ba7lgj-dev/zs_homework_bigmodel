package org.ba7lgj.ccp.common.enums;

/**
 * 平台实名审核状态。
 */
public enum RealAuthStatusEnum
{
    UN_AUTH(0, "未认证"),
    PENDING(1, "待审核"),
    APPROVED(2, "审核通过"),
    REJECTED(3, "审核不通过");

    private final int code;
    private final String desc;

    RealAuthStatusEnum(int code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    public int getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }

    public static RealAuthStatusEnum fromCode(Integer code)
    {
        if (code == null)
        {
            return UN_AUTH;
        }
        for (RealAuthStatusEnum value : values())
        {
            if (value.code == code)
            {
                return value;
            }
        }
        return UN_AUTH;
    }
}
