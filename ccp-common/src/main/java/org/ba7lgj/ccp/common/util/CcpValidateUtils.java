package org.ba7lgj.ccp.common.util;

import java.util.Objects;

/**
 * CCP 通用校验工具。
 */
public final class CcpValidateUtils
{
    private CcpValidateUtils()
    {
    }

    public static boolean isBlank(String value)
    {
        return value == null || value.trim().isEmpty();
    }

    public static boolean equalsAny(Integer source, Integer... values)
    {
        if (source == null || values == null)
        {
            return false;
        }
        for (Integer item : values)
        {
            if (Objects.equals(source, item))
            {
                return true;
            }
        }
        return false;
    }
}
