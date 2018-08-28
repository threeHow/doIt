package com.zc.testActiviti.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.List;

/**
 * @ClassName ProcessDefinitionEntityTest  流程定义的各种测试方法
 * @Description
 * @Author zhujun
 * @Date 2018/8/27 16:32
 */
public class ProcessDefinitionEntityTest {
    /**
     * 功能描述:根据pdid得到ProcessDefinitionEntry(对应数据库表act_re_procdef)
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 16:32
     */
    @Test
    public void testProcessDefinitionEntity() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine.getRepositoryService()
                .getProcessDefinition("shenqing:1:25004");
        System.out.println(pd.getSuspensionState());
    }

    /**
     * 功能描述: 根据pdid得到processDefinitionEntity中的activityimpl
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 16:45
     */
    @Test
    public void testGetActivityImpl() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        /*根据pdid得到processDefinitionEntity(对应数据库表act_re_procdef)*/
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine
                .getRepositoryService()
                .getProcessDefinition("shenqing:1:25004");
        /**
         * ActivityImpl是一个对象
         * 一个activityImpl代表processDefinitionEntity中的一个节点
         * pd.getActivities(); 表示取得所有的节点
         */
        List<ActivityImpl> activityImpls = pd.getActivities();
        for (ActivityImpl activityImpl : activityImpls) {
            System.out.println(activityImpl.getId());
            System.out.println("height:" + activityImpl.getHeight());
            System.out.println("width:" + activityImpl.getWidth());
            System.out.println("X:" + activityImpl.getX());
            System.out.println("Y:" + activityImpl.getY());
            System.out.println();
        }
    }

    /**
     * 功能描述:得到ProcessDefinitionEntity中的所有的ActivityImpl的所有的PvmTransition
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 17:04
     */
    @Test
    public void testSequenceFlow() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
         /*根据pdid得到processDefinitionEntity(对应数据库表act_re_procdef)*/
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine
                .getRepositoryService()
                .getProcessDefinition("shenqing:1:25004");
        /**
         * ActivityImpl是一个对象
         * 一个activityImpl代表processDefinitionEntity中的一个节点
         */
        List<ActivityImpl> activityImpls = pd.getActivities();
        for (ActivityImpl activityImpl : activityImpls) {
            /**
             * 得到一个activityimpl的所有的outgoing
             */
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitions) {
                System.out.println("sequenceFlowId:" + pvmTransition.getId());
            }
        }
    }

    /**
     * 功能描述:得到当前正在执行的流程实例的activityimpl-->PvmTransition
     *
     * @param
     * @return:
     * @auther: ZhuJun
     * @date: 2018/8/27 17:13
     */
    @Test
    public void testQueryActivityImpl_Ing() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
         /*根据pdid得到processDefinitionEntity(查询act_re_procdef)*/
        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine
                .getRepositoryService()
                .getProcessDefinition("shenqing:1:25004");
        //根据piid获取到activityId(piid→PROC_INST_ID_)(查询act_ru_task)
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId("27501")
                .singleResult();
        //pd.findActivit(String activityId) 得到当前正在执行的流程实例的节点
        ActivityImpl activityImpl = pd.findActivity(pi.getActivityId());
        System.out.println("流程实例id:" + pi.getId());
        System.out.println("当前正在执行的节点：" + activityImpl.getId());
        System.out.println("height:" + activityImpl.getHeight());
        System.out.println("width:" + activityImpl.getWidth());
        System.out.println("X:" + activityImpl.getX());
        System.out.println("Y:" + activityImpl.getY());
        System.out.println();
    }
}
