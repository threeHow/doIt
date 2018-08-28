import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @ClassName ActivitiTable
 * @Description 从数据源和流程图中，生成一个数据库表（这个就是Activiti流程控制的关键的数据表）
 * @Author zhujun
 * @Date 2018/8/27 9:49
 */
public class ActivitiTable {

    /**
     * 创建Activiti流的相关的数据库表
     */
    @Test
    public void creatTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();
    }

}
