package com.zc.testActiviti.activiti;/**
 * Created by ZhuJun on 2018/8/27.
 */

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ActivitiUtils  activiti流程的测试方法
 * @Description
 * @Author zhujun
 * @Date 2018/8/27 18:07
 */
public class ActivitiUtils {
    /**
     * 功能描述:当前用户→当前用户正在执行的任务→当前正在执行的任务的piid→该任务所在的流程实例
     *
     * @param assignee
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 18:07
     */
    public static List<ProcessInstance> getPIBUser(String assignee) {
        List<ProcessInstance> pis = new ArrayList<ProcessInstance>();
        //开启流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        /*该用户正在执行的任务*/
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for (Task task : tasks) {
            /*根据task→piid→pi*/
            String piid = task.getProcessInstanceId();
            ProcessInstance pi = processEngine.getRuntimeService()
                    .createProcessInstanceQuery()
                    .processInstanceId(piid)
                    .singleResult();
            pis.add(pi);
        }
        return pis;
    }

    /**
     * 功能描述: 根据当前的登录人能够推导出所在的流程定义
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 18:31
     */
    public static void getProcessInstance(String assignee) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for (Task task : tasks) {
            String pdid = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = processEngine
                    .getRepositoryService()
                    .createProcessDefinitionQuery()
                    .processDefinitionId(pdid)
                    .singleResult();
            System.out.println(processDefinition);
        }
    }

    /**
     * 情况一：当没有进入该节点之前，就可以确定任务的执行人
     * 实例：比如进行“请假申请”的流程时候，最开始执行的就是提交”请假申请“，
     * 那么就需要知道，谁提交的“请假”，很明显，在一个系统中，
     * 谁登陆到系统里面，谁就有提交“请假任务”的提交人，那么执行人就可以确定就是登录人。
     */
    @Test
    public void startDeployTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .name("请假流程：情况一") //流程发布的时候(act_re_deployment中的NAME_字段设置数据)
                .addClasspathResource("shenqing1.bpmn")
                .addClasspathResource("shenqing1.png")
                .deploy();
    }

    /**
     * 功能描述: 启动流程
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/28 9:37
     */
    @Test
    public void testStartPI() {
        /**
         * 流程变量
         *   给<userTask id="请假申请" name="请假申请" activiti:assignee="#{student}"></userTask>
         *     的student赋值
         */
        Map<String, Object> variables = new HashMap();
        variables.put("student", "小明");
        //启动流程
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //startProcessInstanceById(String var1, Map<String, Object> var2) 此方法可以给设置的变量赋值
        processEngine.getRuntimeService().startProcessInstanceById("shenqing1:1:37504", variables);
    }

    /**
     * 功能描述:完成当前正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/28 9:46
     */
    @Test
    public void testFinishTask_Teacher() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("teacher", "李老师");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过当前流程操作人和流程名称，查找到当前流程的(act_ru_task的主键id)
        Task tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("小明")
                .taskDefinitionKeyLike("请假申请")
                .taskName("请假申请")
                .singleResult();
        //完成任务的同时设置下一流程执行人信息
        processEngine.getTaskService().complete(tasks.getId(), variables);
    }

    /**
     * 在完成班主任审批的情况下，给教务处节点赋值
     */
    @Test
    public void testFinishTask_Manager() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("manager", "我是小明的教务处处长");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("50003", variables); //完成任务的同时设置流程变量
    }

    /**
     * 结束流程实例
     */
    @Test
    public void testFinishTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("52503");
    }

    /**
     * 情况二：一个节点任务，
     * 之前是不存在执行人（未知），
     * 只有当符合身份的人，登陆系统，
     * 进入该系统，才能确定执行人。
     */
    @Test
    public void startDeployTest2() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .name("请假流程：情况二") //流程发布的时候(act_re_deployment中的NAME_字段设置数据)
                .addClasspathResource("shenqing2.bpmn")
                .addClasspathResource("shenqing2.png")
                .deploy();
    }

    /**
     * 功能描述: 启动流程
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/28 9:37
     */
    @Test
    public void testStartPI2() {
        //启动流程
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //根据部署的流程名称查询出(act_re_procdef的ID)
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionName("shenqing3")
                .processDefinitionKey("shenqing3")
                .singleResult();
        System.out.println();
        processEngine.getRuntimeService().startProcessInstanceById(pd.getId());
    }

    /**
     * 功能描述:完成当前正在执行的任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/28 9:46
     */
    @Test
    public void testFinishTask_Teacher2() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过当前流程操作人和流程名称，查找到当前流程的(act_ru_task的主键id)
        Task tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("我是班主任")
                .taskDefinitionKeyLike("教务处")
                .taskName("教务处")
                .singleResult();
        //完成任务的同时设置下一流程执行人信息
        processEngine.getTaskService().complete(tasks.getId());
    }
}


