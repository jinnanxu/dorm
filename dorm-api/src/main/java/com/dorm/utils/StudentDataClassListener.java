package com.dorm.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.dorm.dao.mapper.StudentMapper;
import com.dorm.dao.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class StudentDataClassListener extends AnalysisEventListener<Student> {

    private List<Student> datas = new ArrayList<>();

    @Override
    public void invoke(Student data, AnalysisContext context) {

        //写入数据库
        data.setStatus(0);
        datas.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成后做的事情
    }

    public List<Student> getDatas() {
        return datas;
    }

    /**
     * 设置读取的数据集合
     *
     * @param datas 设置读取的数据集合
     **/
    public void setDatas(List<Student> datas) {
        this.datas = datas;
    }
}
