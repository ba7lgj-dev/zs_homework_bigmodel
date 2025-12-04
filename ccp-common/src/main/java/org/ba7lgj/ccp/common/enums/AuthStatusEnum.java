package org.ba7lgj.ccp.common.enums;

/**
 * 认证状态枚举。
 */
public enum AuthStatusEnum
{
    UN_AUTH(0, "未认证"),
    PENDING(1, "待审核"),
    APPROVED(2, "审核通过"),
    REJECTED(3, "审核不通过");

    private final int code;
    private final String desc;

    AuthStatusEnum(int code, String desc)
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

    public static AuthStatusEnum fromCode(Integer code)
    {
        if (code == null)
        {
            return UN_AUTH;
        }
        for (AuthStatusEnum value : values())
        {
            if (value.code == code)
            {
                return value;
            }
        }
        return UN_AUTH;
    }
}
