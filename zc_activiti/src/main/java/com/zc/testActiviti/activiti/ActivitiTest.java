package com.zc.testActiviti.activiti;/**
 * Created by ZhuJun on 2018/8/27.
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @ClassName ActivitiTest
 * @Description 用于进行演示Activiti的首例程序，即描述如何在代码中实现学生进行请假申请，班主任审核，教务处审核
 * @Author zhujun
 * @Date 2018/8/27 10:37
 */
public class ActivitiTest {

    /**
     * 1、部署流程
     * 2、启动流程实例
     * 3、请假人发出请假申请
     * 4、班主任查看任务
     * 5、班主任审批
     * 6、最终的教务处Boss审批
     */
    /**
     * 1：部署一个Activiti流程
     * 运行成功后，查看之前的数据库表，就会发现多了很多内容
     */
    @Test
    public void creatActivitiTask() {
        //加载的那两个内容就是我们之前已经弄好的基础内容
        //得到了流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getRepositoryService()
                    .createDeployment()
                    .addClasspathResource("shenqing.bpmn")
                    .addClasspathResource("shenqing.png")
                    .deploy();
    }

    /**
     * 功能描述: 启动流程实例
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 10:55
     */
    @Test
    public void testStartProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService().startProcessInstanceById("shenqing:1:4");
    }

    /**
     * 功能描述: 完成当前流程的请假申请
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 11:13
     */
    @Test
    public void testQingjia() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete("5002");//此id是act_ru_task的主键id,根据流程的流转不断的变换，代表的流程阶段也不相同
    }

    /**
     * 功能描述: 小明学习的班主任小毛查询当前正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 11:19
     */
    @Test
    public void testQueryTask() {
        //下面代码中的小毛，就是我们之前设计流程图中添加的班主任姓名
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee("小毛").list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     *
     * 功能描述:班主任小毛完成任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 11:31
     */
    @Test
    public void testFinishTask_manager(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete("");//查看act_ru_task数据表 为当前数据表的流程定义是班主任的主键id
    }

    /**
     * 功能描述: 小明学习的教务处大毛查询当前正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 11:19
     */
    @Test
    public void testQueryTasks() {
        //下面代码中的大毛，就是我们之前设计流程图中添加的教务处姓名
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee("大毛").list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     *
     * 功能描述: 教务处大毛完成任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 11:34
     */
    @Test
    public void  testFinishTask_Boss(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete("7502");//查看act_ru_task数据表 为当前数据表的流程定义是教务处的主键id
    }



}
