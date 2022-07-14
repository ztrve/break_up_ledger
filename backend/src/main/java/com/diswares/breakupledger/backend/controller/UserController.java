package com.diswares.breakupledger.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diswares.breakupledger.backend.kernel.proxy.response.InclusionStrategy;
import com.diswares.breakupledger.backend.kernel.proxy.response.annotions.Inclusion;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.GeneralQo;
import com.diswares.breakupledger.backend.qo.user.UserCreateQo;
import com.diswares.breakupledger.backend.qo.user.UserInfoQo;
import com.diswares.breakupledger.backend.qo.user.UserUpdateQo;
import com.diswares.breakupledger.backend.service.UserInfoService;
import com.diswares.breakupledger.backend.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 账号中心
 *
 * @author : GTF
 * @version : 1.0
 * @date : 2022/5/30 17:37
 */
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;

    /**
     * 同步鉴权中心注册的用户
     *
     * @param user 用户信息
     */
    @PostMapping("/sync/absol")
    public void syncAbsolUser(@RequestBody String user) {
        userInfoService.syncAbsolUser(user);
    }

    /**
     * 删除用户
     *
     * @param user 用户Id
     */
    @PostMapping("/delete")
    public void delete(@RequestBody String user) {
        userInfoService.delete(user);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id id
     * @return group.wc.edwards.vo.UserInfoVo
     * @author GTF
     * @date 2022/5/31 11:06
     */
    @GetMapping("/{id}")
    public UserInfo getUserInfoById(@PathVariable Long id) {
        return userInfoService.getUserInfoById(id);
    }

    /**
     * 添加用户
     *
     * @param qo 用户信息
     * @author GTF
     * @date 2022/5/31 11:31
     */
    @PostMapping
    public void registerUser(@RequestBody @Valid UserCreateQo qo, HttpServletRequest request) {
        userInfoService.registerUser(qo, request);
    }

    /**
     * 修改用户信息
     *
     * @param qo 用户信息
     * @author GTF
     * @date 2022/5/31 14:47
     */
    @PatchMapping
    public void modifyUser(@RequestBody @Valid UserUpdateQo qo, HttpServletRequest request) {
        userInfoService.modifyUser(qo, request);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return group.wc.edwards.vo.UserInfoVo
     * @author GTF
     * @date 2022/5/31 11:03
     */
    @GetMapping("/current")
    public UserInfoVo getCurrentUserInfo() {
        return userInfoService.getCurrentUserInfo();
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @author GTF
     * @date 2022/5/31 14:50
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        Assert.state(id != null, "请选择用户!");
        userInfoService.deleteUser(id);
    }

    /**
     * 分页获取用户信息
     *
     * @param page 分页信息
     * @param qo   查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<group.wc.edwards.po.UserInfo>
     * @author GTF
     * @date 2022/6/1 17:04
     */
    @GetMapping
    @Inclusion(InclusionStrategy.PAGE)
    public IPage<UserInfo> selectUserInfoByConditions(Page<UserInfo> page, UserInfoQo qo) {
        return userInfoService.selectUserInfoByConditions(page, qo);
    }

    /**
     * 获取未绑定员工的用户信息
     *
     * @param qo 查询条件
     * @return java.util.List<group.wc.edwards.po.UserInfo>
     * @author GTF
     * @date 2022/6/21 18:08
     */
    @GetMapping("/unbind-employee")
    public List<UserInfo> getUnbindEmployeeUserInfo(GeneralQo qo) {
        return userInfoService.getUnbindEmployeeUserInfo(qo);
    }
}
