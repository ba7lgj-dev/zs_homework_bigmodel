package com.ruoyi.ccp.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.ccp.domain.MiniUser;
import com.ruoyi.ccp.mapper.MiniUserMapper;
import com.ruoyi.ccp.service.IMiniUserService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 小程序用户Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class MiniUserServiceImpl implements IMiniUserService
{
    @Autowired
    private MiniUserMapper miniUserMapper;

    @Override
    public MiniUser selectMiniUserById(Long id)
    {
        return miniUserMapper.selectMiniUserById(id);
    }

    @Override
    public MiniUser selectMiniUserByOpenId(String openId)
    {
        if (StringUtils.isEmpty(openId))
        {
            return null;
        }
        return miniUserMapper.selectMiniUserByOpenId(openId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MiniUser autoRegister(String openId)
    {
        MiniUser existed = selectMiniUserByOpenId(openId);
        if (existed != null)
        {
            return existed;
        }
        MiniUser miniUser = new MiniUser();
        miniUser.setOpenId(openId);
        miniUser.setStatus(1);
        miniUser.setAuthStatus(0);
        miniUser.setCreateTime(DateUtils.getNowDate());
        miniUser.setUpdateTime(DateUtils.getNowDate());
        miniUserMapper.insertMiniUser(miniUser);
        return miniUser;
    }

    @Override
    public List<MiniUser> selectMiniUserList(MiniUser miniUser)
    {
        return miniUserMapper.selectMiniUserList(miniUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertMiniUser(MiniUser miniUser)
    {
        miniUser.setCreateTime(DateUtils.getNowDate());
        miniUser.setUpdateTime(DateUtils.getNowDate());
        if (miniUser.getStatus() == null)
        {
            miniUser.setStatus(1);
        }
        if (miniUser.getAuthStatus() == null)
        {
            miniUser.setAuthStatus(0);
        }
        return miniUserMapper.insertMiniUser(miniUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMiniUser(MiniUser miniUser)
    {
        miniUser.setUpdateTime(DateUtils.getNowDate());
        return miniUserMapper.updateMiniUser(miniUser);
    }

    @Override
    public int changeStatus(Long id, Integer status)
    {
        return miniUserMapper.updateMiniUserStatus(id, status);
    }

    @Override
    public int reviewUser(MiniUser miniUser)
    {
        miniUser.setReviewTime(new Date());
        return miniUserMapper.updateMiniUserReview(miniUser);
    }

    @Override
    public int deleteMiniUserByIds(Long[] ids)
    {
        return miniUserMapper.deleteMiniUserByIds(ids);
    }

    @Override
    public int deleteMiniUserById(Long id)
    {
        return miniUserMapper.deleteMiniUserById(id);
    }
}
