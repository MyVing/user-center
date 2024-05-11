package com.ving.usercenter.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 导入应用用户到数据库
 * @Author ving
 * @Date 2024/5/8 20:41
 */
public class ImportApplicationUser {

    public static void main(String[] args) {
        String fileName = "D:\\JavaProjects\\user-center\\user-center-backend\\src\\main\\resources\\testExcel.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<UserCenterTableInfo> userInfoList =
                EasyExcel.read(fileName).head(UserCenterTableInfo.class).sheet().doReadSync();
        System.out.println("总数 = "+ userInfoList.size());
        Map<String, List<UserCenterTableInfo>> listMap =
                userInfoList.stream().
                        filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername())).
                        collect(Collectors.groupingBy(UserCenterTableInfo::getUsername));
        System.out.println("不重复的昵称数 = "+listMap.keySet().size());

    }
}
