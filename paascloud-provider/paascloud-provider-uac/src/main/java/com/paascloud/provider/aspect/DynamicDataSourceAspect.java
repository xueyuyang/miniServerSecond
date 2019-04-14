/**
 * 
 */
package com.paascloud.provider.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.paascloud.ThreadLocalMap;
import com.paascloud.base.constant.GlobalConstant;
import com.paascloud.core.config.DataSourceHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xyy
 *
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {
    
    @Pointcut("execution(* com.paascloud.provider.service..*.*(..)) && execution(* com.paascloud.provider.manager..*.*(..))")
    public void daoAop() {

    }
    
    @Before("daoAop()")
    public void beforeSwitchDS(JoinPoint joinPoint) {
        // 切换数据源
        DataSourceHolder.setDataSource((String) ThreadLocalMap.get(GlobalConstant.Sys.APP_ID));
    }
    
    @After("daoAop()")
    public void afterSwitchDS(JoinPoint joinPoint) {
        DataSourceHolder.clearDataSource();
        log.info("Restore DataSource to [{}] in Method [{}]",
        DataSourceHolder.getDataSource(), joinPoint.getSignature());
    }

}
