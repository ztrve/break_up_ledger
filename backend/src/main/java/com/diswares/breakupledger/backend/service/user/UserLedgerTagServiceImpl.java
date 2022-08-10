package com.diswares.breakupledger.backend.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diswares.breakupledger.backend.po.ledger.LedgerTag;
import com.diswares.breakupledger.backend.po.user.UserInfo;
import com.diswares.breakupledger.backend.po.user.UserLedgerTag;
import com.diswares.breakupledger.backend.mapper.UserLedgerTagMapper;
import com.diswares.breakupledger.backend.qo.user.UserLedgerTagCreateQo;
import com.diswares.breakupledger.backend.qo.user.UserLedgerTagUpdateQo;
import com.diswares.breakupledger.backend.service.ledger.LedgerTagService;
import com.diswares.breakupledger.backend.util.AuthUtil;
import com.diswares.breakupledger.backend.vo.user.UserLedgerTagVo;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author z_true
 */
@Service
@RequiredArgsConstructor
public class UserLedgerTagServiceImpl extends ServiceImpl<UserLedgerTagMapper, UserLedgerTag>
    implements UserLedgerTagService{
    private final LedgerTagService ledgerTagService;

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

        List<LedgerTag> ledgerTags = ledgerTagService.list();


        LambdaQueryWrapper<UserLedgerTag> query = new LambdaQueryWrapper<>();
        query.eq(UserLedgerTag::getUserId, me.getId());
        List<UserLedgerTag> userLedgerTags = list(query);
        Map<Long, UserLedgerTag> userLedgerTagMap;
        if (ObjectUtils.isEmpty(userLedgerTags)) {
            userLedgerTagMap= userLedgerTags.stream()
                    .collect(Collectors.toMap(UserLedgerTag::getLedgerTagId, v -> v, (a, b) -> a));
        } else {
            userLedgerTagMap = new HashMap<>(16);
        }

        return ledgerTags.stream()
                .map(ledgerTag -> {
                    UserLedgerTagVo vo = new UserLedgerTagVo();
                    vo.setLedgerTagId(ledgerTag.getId());
                    vo.setTag(ledgerTag.getTag());
                    UserLedgerTag userLedgerTag = userLedgerTagMap.get(ledgerTag.getId());
                    if (ObjectUtils.isEmpty(userLedgerTag)) {
                        vo.setIsDefaultTag(false);
                    } else {
                        vo.setIsDefaultTag(userLedgerTag.getIsDefaultTag());
                    }
                    return vo;
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
//        userLedgerTag.setTag(userLedgerTagCreateQo.getTag());
        userLedgerTag.setIsDefaultTag(userLedgerTagCreateQo.getIsDefaultTag());
        save(userLedgerTag);

        return getOneDetail(userLedgerTag.getId());
    }

    @Override
    public List<UserLedgerTagVo> updateUserDefaultLedgerTags(List<UserLedgerTagUpdateQo> userLedgerTagUpdateQoList) {
        UserInfo me = AuthUtil.currentUserInfo();

        LambdaQueryWrapper<UserLedgerTag> query = new LambdaQueryWrapper<>();
        query.eq(UserLedgerTag::getUserId, me.getId());
        remove(query);
        query.clear();

        List<UserLedgerTag> userLedgerTags = userLedgerTagUpdateQoList.stream()
                .filter(qo -> !qo.getIsDefaultTag())
                .map(qo -> {
                    UserLedgerTag po = new UserLedgerTag();
                    po.setUserId(me.getId());
                    po.setLedgerTagId(qo.getLedgerTagId());
                    po.setIsDefaultTag(true);
                    return po;
                })
                .collect(Collectors.toList());
        saveBatch(userLedgerTags);

        return myList();
    }
}




