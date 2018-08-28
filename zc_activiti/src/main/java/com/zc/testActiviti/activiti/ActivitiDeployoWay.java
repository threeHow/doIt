package com.zc.testActiviti.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @ClassName Activiti流程部署类
 * @Description
 * @Author zhujun
 * @Date 2018/8/27 13:44
 */
public class ActivitiDeployoWay {

    /**
     * 功能描述: 部署流程的方法一(通过bpmn和png资源进行部署)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 13:47
     */
    @Test
    public void testDeployFromClasspath() {
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("shenqing.bpmn")
                .addClasspathResource("shenqing.png")
                .deploy();
    }

    /**
     * 功能描述: 通过inputstream完成部署
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 13:51
     */
    @Test
    public void testDeployFromInputStream() {
        InputStream bpmnstream = this.getClass().getClassLoader().getResourceAsStream("shenqing.bpmn");
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addInputStream("shenqing.bpmn", bpmnstream)
                .deploy();
    }

    /**
     * 功能描述: 通过zipinputstream完成部署
     * ps:需要将bpmn和png文件压缩成zip文件，然后放在项目制定的目录下
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 13:57
     */
    @Test
    public void testDeployFromzipInputStream() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("shenqing.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    /**
     * 功能描述: 删除已经部署的Activiti流程
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 14:06
     */
    @Test
    public void testDelete() {
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //第一个参数是部署流程的ID(DEPLOYMENT_ID_),第二个true表示是级联删除(删除有关系的所有表)
        processEngine.getRepositoryService().deleteDeployment("35001", true);
    }

    /**
     * 功能描述: 根据名称查询流程部署
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 14:12
     */
    @Test
    public void testQueryDeploymentByName() {
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> deployments = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()//按照部署时间排序
                .desc()//按照降序排序
                .deploymentName("请假流程")
                .list();
        for (Deployment deployment : deployments) {
            System.out.println(deployment.getId());
        }
    }

    /**
     * 功能描述: 查询所有部署的流程
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 14:31
     */
    @Test
    public void queryAllDeployment() {
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> deployments = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()
                .desc()
                .list();
        for (Deployment deployment : deployments) {
            System.out.println("流程Id:" + deployment.getId() + ",流程部署名称:" + deployment.getName());
        }
    }

    /**
     * 功能描述: 查询所有的流程定义
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 14:35
     */
    @Test
    public void testQueryAllPD() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> pdList = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition pd : pdList) {
            System.out.println(pd.getName());
        }
    }

    /**
     * 功能描述: 查看流程图
     * 根据deploymentId和name(在act_ge_bytearray数据表中)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 14:42
     */
    @Test
    public void testShowImage() throws Exception {
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream inputStream = processEngine.getRepositoryService()
                /**
                 *
                 *部署的deploymentID(对应act_ge_bytearray表的DEPLOYMENT_ID_),文件的名称和路径(对应act_ge_bytearray表的NAME_)
                 */
                .getResourceAsStream("22501", "shenqing/shenqing.png");
        OutputStream out = new FileOutputStream("D:/processing.png");
        int b = -1;
        while ((b = inputStream.read()) != -1) {
            out.write(b);
        }
        inputStream.close();
        out.close();
    }

    /**
     * 功能描述:根据pdid查看图片(在act_re_prodef数据库表中 ID_字段)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:02
     */
    @Test
    public void testShowImage2() throws Exception {
        //获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream in = processEngine.getRepositoryService()
                //在act_re_prodef数据库表中 ID_字段
                .getProcessDiagram("shenqing:1:17504");
        OutputStream ou = new FileOutputStream("D:/processing2.png");
        int b = -1;
        while ((b = in.read()) != -1) {
            ou.write(b);
        }
        in.close();
        ou.close();
    }

    /**
     * 功能描述: 生成bpmn文件(在act_re_prodef数据表中)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:08
     */
    @Test
    public void testShowBpmn() throws Exception {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream in = processEngine.getRepositoryService()
                //在act_re_prodef数据库表中 ID_字段
                .getProcessModel("shenqing:1:17504");
        OutputStream ou = new FileOutputStream("D:/processing.bpmn");
        int b = -1;
        while ((b = in.read()) != -1) {
            ou.write(b);
        }
        in.close();
        ou.close();
    }

    /**
     * 功能描述: 启动流程实例方法一（通过pdid）
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:37
     */
    @Test
    public void testStartProcessInstanceByPID() {
        //启动流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine
                .getRuntimeService()
                .startProcessInstanceById("shenqing1:1:37504");
        System.out.println(processInstance.getId());
    }


    /**
     * 功能描述:开启流程实例方法二(根据pdkey启动流程实例，默认启动最高版本的)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:42
     */
    @Test
    public void testStartPIByPDKEY() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("shenqing"); //这个字段对应上面那个数据库中的Key字段
        System.out.println("流程主键ID：" + processInstance.getId() +
                ",流程部署名称:" + processInstance.getName());
    }

    /**
     * 功能描述: 完成当前任务(请假申请→班主任→教务处→结束)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:48
     */
    @Test
    public void testFinishTask() {
        //开启流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete("30002");
    }

    /**
     * 功能描述:查询任务
     * 根据任务的执行人查询正在执行的任务(通过act_ru_task表数据)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 15:55
     */
    @Test
    public void testQueryTaskByAssignee() {
        //流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        /**
         *
         * 功能描述:当前班主任小毛人这个人当前正在执行的所有的任务
         *
         */
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .orderByTaskCreateTime()
                .desc()
                .taskAssignee("小毛")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    /**
     * 功能描述: 查询所有正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 16:02
     */
    @Test
    public void testQueryTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .list();
        for (Task task : tasks) {
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    /**
     * 功能描述:根据piid查询任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 16:07
     */
    @Test
    public void testQueryTaskByPIID() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .executionId("27501")
                .list();
        for (Task task : tasks) {//因为没有并发，所以就有一个
            System.out.println(task.getName());
        }
    }
    
    /**
     *
     * 功能描述:根据piid得到当前正在执行的流程实例的正在活动的节点
     *
     * @param 
     * @return: 
     * @auther: ZhuJun
     * @date: 2018/8/27 16:10
     */
    @Test
    public void testActivity(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        ProcessInstance pi=processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId("27501")
                .singleResult();
        String activityId=pi.getActivityId();//当前流程正在执行的activiyuId
        System.out.println(activityId);
    }

    /**
     *
     * 功能描述:查看已经完成的任务和当前正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 16:20
     */
    @Test
    public void findHistoryTask(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        //如果只想获取到已经只执行完成的，那么就假如completed这个过滤条件
        List<HistoricTaskInstance> historicTaskInstance=processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskDeleteReason("completed")
                .list();
        //如果想查看所有任务，那就不需要加completed条件
        List<HistoricTaskInstance> historicTaskInstance2=processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .list();
        System.out.println("执行完成的任务数量："+historicTaskInstance.size());
        System.out.println("所有的总任务数(执行完和当前未执行完)的任务数量："+historicTaskInstance2.size());
    }
}
