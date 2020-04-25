package com.sunxn.news.webcrawler.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description: 动态的获取Bean对象，不能使用依赖注入了
 *  我们获取的Bean是从Spring容器中获取的，所以要获取Spring上下文，即ApplicationContext。
 *      首先：编写的工具类要被Spring管理，实现ApplicationContextAware接口，
 *          这样在Spring加载的时候，会执行setApplicationContext方法将ApplicationContext注入到当前的工具类。
 *      注意：如果获取的Bean是singleton，那么工具类获取的也是singleton，
 *          如果是propotype的话，Spring每次会跟你new一个出来
 * @data: 2020/4/25 13:18
 * @author: xiaoNan
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public SpringContextUtil() {

    }

    public static <T> T getBean(String name) {
        return (T) ctx.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return ctx.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ctx.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        return ctx.getBean(requiredType, args);
    }
}
