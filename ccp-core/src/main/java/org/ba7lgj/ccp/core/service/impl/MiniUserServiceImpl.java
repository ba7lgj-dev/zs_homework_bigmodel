package org.ba7lgj.ccp.core.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.common.enums.AuthStatusEnum;
import org.ba7lgj.ccp.core.mapper.MiniUserMapper;
import org.ba7lgj.ccp.core.service.IMiniUserService;

/**
 * 小程序用户业务实现。
 */
@Service
public class MiniUserServiceImpl implements IMiniUserService
{
    private final MiniUserMapper miniUserMapper;

    public MiniUserServiceImpl(MiniUserMapper miniUserMapper)
    {
        this.miniUserMapper = miniUserMapper;
    }

    @Override
    public MiniUser selectMiniUserById(Long id)
    {
        return miniUserMapper.selectMiniUserById(id);
    }

    @Override
    public MiniUser selectMiniUserByOpenId(String openId)
    {
        if (StringUtils.isBlank(openId))
        {
            return null;
        }
        return miniUserMapper.selectMiniUserByOpenId(openId);
    }

    @Override
    public List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO)
    {
        return miniUserMapper.selectMiniUserList(queryDTO);
    }

    @Override
    public List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO)
    {
        return miniUserMapper.selectMiniUserExportList(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MiniUser autoRegisterByOpenId(String openId)
    {
        MiniUser exist = selectMiniUserByOpenId(openId);
        if (exist != null)
        {
            return exist;
        }
        MiniUser miniUser = new MiniUser();
        miniUser.setOpenId(openId);
        miniUser.setStatus(1);
        miniUser.setAuthStatus(AuthStatusEnum.UN_AUTH.getCode());
        miniUserMapper.insertMiniUser(miniUser);
        return miniUser;
    }

    @Override
    public int updateMiniUser(MiniUser user)
    {
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int changeStatus(Long id, Integer status)
    {
        MiniUser user = new MiniUser();
        user.setId(id);
        user.setStatus(status);
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int reviewUser(Long id, Integer targetAuthStatus, String failReason, Long reviewBy, Date reviewTime)
    {
        if (targetAuthStatus == null || (!AuthStatusEnum.APPROVED.getCode().equals(targetAuthStatus)
                && !AuthStatusEnum.REJECTED.getCode().equals(targetAuthStatus)))
        {
            throw new IllegalArgumentException("targetAuthStatus must be 2 or 3");
        }
        if (AuthStatusEnum.REJECTED.getCode().equals(targetAuthStatus) && StringUtils.isBlank(failReason))
        {
            throw new IllegalArgumentException("failReason is required when rejected");
        }
        MiniUser user = new MiniUser();
        user.setId(id);
        user.setAuthStatus(targetAuthStatus);
        user.setAuthFailReason(failReason);
        user.setReviewBy(reviewBy);
        user.setReviewTime(reviewTime);
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int deleteMiniUserById(Long id)
    {
        return miniUserMapper.deleteMiniUserById(id);
    }

    @Override
    public int deleteMiniUserByIds(Long[] ids)
    {
        return miniUserMapper.deleteMiniUserByIds(ids);
    }
}
