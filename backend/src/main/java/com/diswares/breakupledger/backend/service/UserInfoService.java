package com.diswares.breakupledger.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.GeneralQo;
import com.diswares.breakupledger.backend.qo.user.UserCreateQo;
import com.diswares.breakupledger.backend.qo.user.UserInfoQo;
import com.diswares.breakupledger.backend.qo.user.UserUpdateQo;
import com.diswares.breakupledger.backend.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author GTF
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 根据id获取用户信息
     * @author GTF
     * @date 2022/5/31 11:06
     * @param id 用户id
     * @return group.wc.edwards.vo.UserInfoVo
     */
    UserInfo getUserInfoById(Long id);

    /**
     * 添加用户
     * @author GTF
     * @date 2022/5/31 11:31
     * @param qo 用户信息
     * @param request request
     */
    void registerUser(UserCreateQo qo, HttpServletRequest request);

    /**
     * 获取当前登录用户信息
     * @author GTF
     * @date 2022/5/31 11:03
     * @return group.wc.edwards.vo.UserInfoVo
     */
    UserInfoVo getCurrentUserInfo();

    /**
     * 修改用户信息
     * @author GTF
     * @date 2022/5/31 14:47
     * @param qo 用户信息
     * @param request request
     */
    void modifyUser(UserUpdateQo qo, HttpServletRequest request);

    /**
     * 删除用户
     * @author GTF
     * @date 2022/5/31 14:50
     * @param id 用户id
     */
    void deleteUser(Long id);

    /**
     * 分页获取用户信息
     * @author GTF
     * @date 2022/6/1 17:04
     * @param page 分页信息
     * @param qo 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<group.wc.edwards.vo.UserInfoVo>
     */
    IPage<UserInfo> selectUserInfoByConditions(Page<UserInfo> page, UserInfoQo qo);

    /**
     * 同步鉴权中心注册的用户
     * @param user 用户信息
     */
    void syncAbsolUser(String user);

    /**
     * 删除用户
     * @param user 用户Id
     */
    void delete(String user);

    /**
     * 获取未绑定员工的用户信息
     * @author GTF
     * @date 2022/6/21 18:08
     * @param qo 查询条件
     * @return java.util.List<group.wc.edwards.po.UserInfo>
     */
    List<UserInfo> getUnbindEmployeeUserInfo(GeneralQo qo);
}
