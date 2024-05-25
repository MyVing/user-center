package com.ving.usercenter.service;

import com.ving.usercenter.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类测试
 *
 * @Author ving
 * @Date 2024/5/24 17:42
 */
public class AlgorithmUtilsTest {



    @Test
    void testCompareTags() {
        List<String> tagList1 = Arrays.asList("Java", "大一", "男");
        List<String> tagList2 = Arrays.asList("Java", "大二", "女");
        List<String> tagList3 = Arrays.asList("python", "大二", "女");
        int score1 = AlgorithmUtils.minDistance(tagList1,tagList2);
        int score2 = AlgorithmUtils.minDistance(tagList1,tagList3 );
        System.out.println(score1);
        System.out.println(score2);
    }

}
