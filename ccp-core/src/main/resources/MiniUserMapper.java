package org.ba7lgj.ccp.core.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;

/**
 * 小程序用户 Mapper 接口。
 */
@Mapper
public interface MiniUserMapper
{
    MiniUser selectMiniUserById(Long id);

    MiniUser selectMiniUserByOpenId(String openId);

    List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO);

    List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO);

    int insertMiniUser(MiniUser user);

    int updateMiniUser(MiniUser user);

    int deleteMiniUserById(Long id);

    int deleteMiniUserByIds(Long[] ids);
}
