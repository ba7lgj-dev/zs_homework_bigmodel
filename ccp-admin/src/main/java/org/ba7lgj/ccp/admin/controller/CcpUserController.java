package org.ba7lgj.ccp.admin.controller;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserAuditDTO;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.core.service.IMiniUserService;
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
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 小程序用户后台管理。
 */
@RestController
@RequestMapping("/ccp/user")
public class CcpUserController extends BaseController
{
    @Autowired
    private IMiniUserService miniUserService;

    @PreAuthorize("@ss.hasPermi('ccp:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniUserQueryDTO query)
    {
        startPage();
        List<MiniUser> list = miniUserService.selectMiniUserList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return AjaxResult.success(miniUserService.selectMiniUserById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:add')")
    @PostMapping
    public AjaxResult add(@RequestBody MiniUser miniUser)
    {
        return toAjax(miniUserService.updateMiniUser(miniUser));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody MiniUser miniUser)
    {
        miniUser.setOpenId(null);
        return toAjax(miniUserService.updateMiniUser(miniUser));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:remove')")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(miniUserService.deleteMiniUserById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:remove')")
    @DeleteMapping("/batch/{ids}")
    public AjaxResult removeBatch(@PathVariable Long[] ids)
    {
        if (ArrayUtils.isEmpty(ids))
        {
            return AjaxResult.error("ids cannot be empty");
        }
        return toAjax(miniUserService.deleteMiniUserByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:changeStatus')")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MiniUser miniUser)
    {
        return toAjax(miniUserService.changeStatus(miniUser.getId(), miniUser.getStatus()));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:review')")
    @PutMapping("/review")
    public AjaxResult review(@RequestBody MiniUserAuditDTO auditDTO)
    {
        Long reviewBy = SecurityUtils.getUserId();
        return toAjax(miniUserService.reviewUser(auditDTO.getId(), auditDTO.getTargetAuthStatus(), auditDTO.getAuthFailReason(), reviewBy, new java.util.Date()));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:export')")
    @GetMapping("/export")
    public AjaxResult export(MiniUserQueryDTO query)
    {
        List<MiniUser> list = miniUserService.selectMiniUserExportList(query);
        ExcelUtil<MiniUser> util = new ExcelUtil<>(MiniUser.class);
        return util.exportExcel(list, "小程序用户数据");
    }
}
