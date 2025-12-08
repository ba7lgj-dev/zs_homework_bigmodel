package org.ba7lgj.ccp.core.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.common.dto.RealAuthReviewDTO;
import org.ba7lgj.ccp.common.dto.RealAuthSubmitDTO;
import org.ba7lgj.ccp.common.enums.RealAuthStatusEnum;
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
        miniUser.setRealAuthStatus(RealAuthStatusEnum.UN_AUTH.getCode());
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
    public int submitRealAuth(RealAuthSubmitDTO dto)
    {
        MiniUser user = new MiniUser();
        user.setId(dto.getUserId());
        user.setIdCardName(dto.getIdCardName());
        user.setIdCardNumber(dto.getIdCardNumber());
        user.setFaceImageUrl(dto.getFaceImageUrl());
        user.setRealAuthStatus(RealAuthStatusEnum.PENDING.getCode());
        user.setRealAuthFailReason(null);
        user.setRealAuthReviewBy(null);
        user.setRealAuthReviewTime(null);
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int reviewRealAuth(RealAuthReviewDTO dto)
    {
        if (dto.getTargetStatus() == null || (RealAuthStatusEnum.APPROVED.getCode() != dto.getTargetStatus()
                && RealAuthStatusEnum.REJECTED.getCode() != dto.getTargetStatus()))
        {
            throw new IllegalArgumentException("targetStatus must be 2 or 3");
        }
        if (RealAuthStatusEnum.REJECTED.getCode() == dto.getTargetStatus() && StringUtils.isBlank(dto.getFailReason()))
        {
            throw new IllegalArgumentException("failReason is required when rejected");
        }
        MiniUser user = new MiniUser();
        user.setId(dto.getUserId());
        user.setRealAuthStatus(dto.getTargetStatus());
        user.setRealAuthFailReason(dto.getFailReason());
        user.setRealAuthReviewBy(dto.getReviewBy());
        user.setRealAuthReviewTime(new Date());
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public Integer getRealAuthStatus(Long userId)
    {
        MiniUser miniUser = selectMiniUserById(userId);
        return miniUser == null ? null : miniUser.getRealAuthStatus();
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
