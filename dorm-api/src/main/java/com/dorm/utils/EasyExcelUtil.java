package com.dorm.utils;

import com.alibaba.excel.EasyExcel;
import com.dorm.dao.model.Student;
import com.alibaba.excel.read.listener.ReadListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class EasyExcelUtil {
    public static void main(String[] args) {
//        String fileName = "example.xlsx"; // Excel文件路径
//        readExcel(fileName, Student.class, new StudentDataClassListener());
    }

    public static void readExcel(InputStream inputStream, Class clazz, ReadListener listener) {
        EasyExcel.read(inputStream, clazz, listener).sheet().doRead();

//        EasyExcel.read(fileName, head, listener).sheet().doRead();
    }
}
