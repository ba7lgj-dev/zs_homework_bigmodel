package org.ba7lgj.ccp.core.service;

import java.util.List;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.common.dto.RealAuthReviewDTO;
import org.ba7lgj.ccp.common.dto.RealAuthSubmitDTO;

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

    int submitRealAuth(RealAuthSubmitDTO dto);

    int reviewRealAuth(RealAuthReviewDTO dto);

    Integer getRealAuthStatus(Long userId);

    int deleteMiniUserById(Long id);

    int deleteMiniUserByIds(Long[] ids);
}
