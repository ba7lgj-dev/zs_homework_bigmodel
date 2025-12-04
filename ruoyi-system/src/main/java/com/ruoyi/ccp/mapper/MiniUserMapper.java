package com.ruoyi.ccp.mapper;

import java.util.List;
import com.ruoyi.ccp.domain.MiniUser;

/**
 * 小程序用户Mapper接口
 *
 * @author ruoyi
 */
public interface MiniUserMapper
{
    /**
     * 查询小程序用户
     *
     * @param id 主键
     * @return 小程序用户
     */
    public MiniUser selectMiniUserById(Long id);

    /**
     * 根据openid查询用户
     *
     * @param openId openid
     * @return 小程序用户
     */
    public MiniUser selectMiniUserByOpenId(String openId);

    /**
     * 查询小程序用户列表
     *
     * @param miniUser 小程序用户
     * @return 小程序用户集合
     */
    public List<MiniUser> selectMiniUserList(MiniUser miniUser);

    /**
     * 新增小程序用户
     *
     * @param miniUser 小程序用户
     * @return 结果
     */
    public int insertMiniUser(MiniUser miniUser);

    /**
     * 修改小程序用户
     *
     * @param miniUser 小程序用户
     * @return 结果
     */
    public int updateMiniUser(MiniUser miniUser);

    /**
     * 批量删除小程序用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMiniUserByIds(Long[] ids);

    /**
     * 删除小程序用户信息
     *
     * @param id 主键
     * @return 结果
     */
    public int deleteMiniUserById(Long id);

    /**
     * 修改用户状态
     *
     * @param id 主键
     * @param status 状态
     * @return 结果
     */
    public int updateMiniUserStatus(Long id, Integer status);

    /**
     * 审核更新
     *
     * @param miniUser 用户
     * @return 结果
     */
    public int updateMiniUserReview(MiniUser miniUser);
}
