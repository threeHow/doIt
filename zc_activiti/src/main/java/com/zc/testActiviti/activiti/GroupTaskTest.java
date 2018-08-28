package com.zc.testActiviti.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @ClassName GroupTaskTest 组任务的测试方法
 * @Description
 * @Author zhujun
 * @Date 2018/8/28 10:48
 */
public class GroupTaskTest {
    /**
     * 主要是对于某些任务流程中，有N个人，但是只需要其中的某一个通过，
     * 则该任务就通过了，所以针对这样的业务需求，就有如下的内容
     */
    @Test
    public void deployTashTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("caiwu.bpmn")
                .addClasspathResource("caiwu.png")
                .name("组任务的测试")
                .deploy();
    }

    /**
     * 当启动完流程实例以后，进入了"电脑维修"节点，该节点是一个组任务
     * 这个时候，组任务的候选人就会被插入到两张表中
     * act_ru_identitylink  存放的是当前正在执行的组任务的候选人
     * act_hi_identitylink
     */
    @Test
    public void processTaskStartTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("财务")
                .processDefinitionName("财务")
                .singleResult();
        processEngine.getRuntimeService().startProcessInstanceById(pd.getId());
    }

    /**
     * 对于act_hi_identitylink表，根据任务ID，即TASK_ID字段查询候选人
     */
    @Test
    public void testQueryCandidateByTaskId() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .taskName("财务报账")
                .taskDefinitionKey("财务报账")
                .singleResult();
        List<IdentityLink> IdentityLinks = processEngine.getTaskService().getIdentityLinksForTask(task.getId());
        for (IdentityLink link : IdentityLinks) {
            System.out.println(link.getUserId());
        }
    }

    /**
     * 对于act_hi_identitylink表，根据候选人,即USER_ID_查看组任务
     */
    @Test
    public void testQueryTaskByCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskCandidateUser("王二麻子")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getName());
        }
    }

    /**
     * 候选人中的其中一个人认领任务
     */
    @Test
    public void testClaimTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .taskName("财务报账")
                .taskDefinitionKey("财务报账")
                .singleResult();
        processEngine.getTaskService()
                /**
                 * 第一个参数为taskId
                 * 第二个参数为认领人
                 */
                .claim(task.getId(), "王二麻子");
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
                .taskAssignee("王二麻子")
                .taskDefinitionKeyLike("财务报账")
                .taskName("财务报账")
                .singleResult();
        //完成任务的同时设置下一流程执行人信息
        processEngine.getTaskService().complete(tasks.getId());
    }

    /**
     *
     * 功能描述:删除任务
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/28 11:06
     */
    @Test
    public void testDelete(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        //第一个参数是部署流程的ID(DEPLOYMENT_ID_),第二个true表示是级联删除(删除有关系的所有表)
        List<Deployment> deploymentList=processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()
                .desc()
                .list();
        for(Deployment deployment:deploymentList){
            processEngine.getRepositoryService().deleteDeployment(deployment.getId(),true);
        }

    }
}



