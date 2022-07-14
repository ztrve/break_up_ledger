package com.diswares.breakupledger.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diswares.breakupledger.backend.po.UserInfo;
import com.diswares.breakupledger.backend.qo.GeneralQo;
import com.diswares.breakupledger.backend.qo.user.UserInfoQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GTF
 * @Entity group.wc.edwards.po.UserInfo
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
    * 分页查询用户
    * @author GTF
    * @date 2022/6/2 10:09
    * @param page 分页对象
    * @param qo 查询对象
    * @return com.baomidou.mybatisplus.core.metadata.IPage<group.wc.edwards.vo.UserInfoVo>
    */
    IPage<UserInfo> selectUserInfoByConditions(Page<UserInfo> page, @Param("qo") UserInfoQo qo);

    /**
     * 获取未绑定员工的用户信息
     * @author GTF
     * @date 2022/6/21 18:08
     * @param qo 查询条件
     * @return java.util.List<group.wc.edwards.po.UserInfo>
     */
    List<UserInfo> getUnbindEmployeeUserInfo(@Param("qo") GeneralQo qo);
}




