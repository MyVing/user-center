package com.ving.usercenter.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;
import java.util.Map;

/**
 * 导入Excel
 * @Author ving
 * @Date 2024/5/8 17:29
 */
public class ImportExcel {

    /**
     * 读取数据
     * @param args
     */
    public static void main(String[] args) {

        String fileName = "D:\\JavaProjects\\user-center\\user-center-backend\\src\\main\\resources\\testExcel.xlsx";

       //n readByListener(fileName);
        synchronousRead(fileName);

    }

    /**
     *  监听器读取
     * @param fileName
     */
    public static void readByListener( String fileName ) {
        EasyExcel.read(fileName,UserCenterTableInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public static void synchronousRead( String fileName ) {
      
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<UserCenterTableInfo> totalDataList = EasyExcel.read(fileName).head(UserCenterTableInfo.class).sheet().doReadSync();

        for (UserCenterTableInfo tableInfo : totalDataList) {
            System.out.println(tableInfo);
        }
    }

}
