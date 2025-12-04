package org.ba7lgj.ccp.core.service;

import java.util.Date;
import java.util.List;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;

/**
 * 小程序用户业务接口。
 */
public interface IMiniUserService
{
    MiniUser selectMiniUserById(Long id);

    MiniUser selectMiniUserByOpenId(String openId);

    List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO);

    List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO);

    MiniUser autoRegisterByOpenId(String openId);

    int updateMiniUser(MiniUser user);

    int changeStatus(Long id, Integer status);

    int reviewUser(Long id, Integer targetAuthStatus, String failReason, Long reviewBy, Date reviewTime);

    int deleteMiniUserById(Long id);

    int deleteMiniUserByIds(Long[] ids);
}
