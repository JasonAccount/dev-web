package web.common;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

public class AppInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // TODO Spring容器初始化之前执行操作

        // 设置项目运行必须的属性
        /*configurableApplicationContext.getEnvironment().setRequiredProperties("requireProperties");*/

        // 配置BeanFactory
        ((AbstractRefreshableApplicationContext)configurableApplicationContext).setAllowBeanDefinitionOverriding(false);
        ((AbstractRefreshableApplicationContext)configurableApplicationContext).setAllowCircularReferences(true);

        System.out.println("*****************AppInitializing*****************");
    }
}
