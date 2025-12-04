package com.ruoyi.ccp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.ccp.domain.MiniUser;
import com.ruoyi.ccp.service.IMiniUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 小程序用户Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/ccp/user")
public class CcpUserController extends BaseController
{
    @Autowired
    private IMiniUserService miniUserService;

    /**
     * 查询小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniUser miniUser)
    {
        startPage();
        List<MiniUser> list = miniUserService.selectMiniUserList(miniUser);
        return getDataTable(list);
    }

    /**
     * 导出小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:export')")
    @Log(title = "小程序用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MiniUser miniUser)
    {
        List<MiniUser> list = miniUserService.selectMiniUserList(miniUser);
        ExcelUtil<MiniUser> util = new ExcelUtil<MiniUser>(MiniUser.class);
        return util.exportExcel(list, "mini_user");
    }

    /**
     * 获取小程序用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(miniUserService.selectMiniUserById(id));
    }

    /**
     * 新增小程序用户
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:add')")
    @Log(title = "小程序用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MiniUser miniUser)
    {
        return toAjax(miniUserService.insertMiniUser(miniUser));
    }

    /**
     * 修改小程序用户
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:edit')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MiniUser miniUser)
    {
        return toAjax(miniUserService.updateMiniUser(miniUser));
    }

    /**
     * 删除小程序用户
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:remove')")
    @Log(title = "小程序用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(miniUserService.deleteMiniUserByIds(ids));
    }

    /**
     * 修改账号状态
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:changeStatus')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MiniUser miniUser)
    {
        if (miniUser.getId() == null || miniUser.getStatus() == null)
        {
            return AjaxResult.error("参数异常");
        }
        return toAjax(miniUserService.changeStatus(miniUser.getId(), miniUser.getStatus()));
    }

    /**
     * 审核用户
     */
    @PreAuthorize("@ss.hasPermi('ccp:user:review')")
    @Log(title = "小程序用户审核", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody MiniUser miniUser)
    {
        if (miniUser.getId() == null || miniUser.getAuthStatus() == null)
        {
            return AjaxResult.error("参数异常");
        }
        if (miniUser.getAuthStatus() == 3 && StringUtils.isEmpty(miniUser.getAuthFailReason()))
        {
            return AjaxResult.error("请填写审核失败原因");
        }
        miniUser.setReviewBy(SecurityUtils.getUserId());
        return toAjax(miniUserService.reviewUser(miniUser));
    }
}
