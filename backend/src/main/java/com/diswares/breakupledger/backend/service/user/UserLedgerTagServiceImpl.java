package com.diswares.breakupledger.backend.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.po.user.UserLedgerTag;
import com.diswares.breakupledger.backend.mapper.UserLedgerTagMapper;
import com.diswares.breakupledger.backend.qo.user.UserLedgerTagCreateQo;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.user.UserLedgerTagVo;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author z_true
 */
@Service
public class UserLedgerTagServiceImpl extends ServiceImpl<UserLedgerTagMapper, UserLedgerTag>
    implements UserLedgerTagService{

    @Override
    public UserLedgerTagVo getOneDetail(Long id) {
        UserLedgerTag po = getById(id);
        UserLedgerTagVo vo = new UserLedgerTagVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }

    @Override
    public List<UserLedgerTagVo> myList() {
        UserInfo me = AuthUtil.currentUserInfo();
        LambdaQueryWrapper<UserLedgerTag> query = new LambdaQueryWrapper<>();
        query.eq(UserLedgerTag::getUserId, me.getId());
        List<UserLedgerTag> list = list(query);
        if (ObjectUtils.isEmpty(list)) {
            return null;
        }
        return list.stream()
                .map(userLedgerTag -> {
                    UserLedgerTagVo userLedgerTagVo = new UserLedgerTagVo();
                    BeanUtils.copyProperties(userLedgerTag, userLedgerTagVo);
                    return userLedgerTagVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserLedgerTagVo createOne(UserLedgerTagCreateQo userLedgerTagCreateQo) {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<UserLedgerTag> query = new LambdaQueryWrapper<>();
        query.eq(UserLedgerTag::getUserId, me.getId())
                .eq(UserLedgerTag::getIsDefaultTag, true);
        int defaultTagCount = count(query);
        int maxDefaultTagSize = 5;
        Assert.isTrue(defaultTagCount <= maxDefaultTagSize, "默认标签数量最多存在" + maxDefaultTagSize + "个");

        UserLedgerTag userLedgerTag = new UserLedgerTag();
        userLedgerTag.setUserId(me.getId());
        userLedgerTag.setTag(userLedgerTagCreateQo.getTag());
        userLedgerTag.setIsDefaultTag(userLedgerTagCreateQo.getIsDefaultTag());
        save(userLedgerTag);

        return getOneDetail(userLedgerTag.getId());
    }
}




