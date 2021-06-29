package com.ssh.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component(value = "logAop")

public class LogAop {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After("execution(* com.ssh.controller..*.*(..))")
    public void loginBefore(JoinPoint point) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request.getSession().getAttribute("LOGIN_USER")!=null&&request.getSession().getAttribute("LOGIN_USER")!=""){
            String sql = "insert  into log(username,action,time)  values(?,?,?)";
            jdbcTemplate.update(sql, new Object[] {
                    request.getSession().getAttribute("LOGIN_USER"),
                    point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName(), new Date()
            });
        }
//        System.out.println("@Before：目标方法为：" +
//                           point.getSignature().getDeclaringTypeName() +
//                           "." + point.getSignature().getName());
//        System.out.println("@Before：参数为：" + Arrays.toString(point.getArgs()));
//        System.out.println("@Before：被织入的目标对象为：" + point.getTarget());
//        System.out.println("前置方法执行了");
    }
    @Before("execution(* com.ssh.controller.UserController.exitLogin(..))")
    public void exitLoginBefore(JoinPoint point) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String sql = "insert  into log(username,action,time)  values(?,?,?)";
        jdbcTemplate.update(sql, new Object[] {
                request.getSession().getAttribute("LOGIN_USER"),
                point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName(), new Date()
        });
    }

//    @After("execution(* com.jl.service.ServiceA.aaa(..))")
//    public void After(){
//        System.out.println("后置方法执行了");
//    }
//    @Around("execution(* com.jl.service.ServiceA.aaa(..))")
//    public Object biz1Around(ProceedingJoinPoint point) throws Throwable{
//        arountbefor();
//        long time =System.currentTimeMillis();
//        //执行链接点
//        Object obj= point.proceed();
//        arountafter();
//        System.out.println("环绕消耗的时间          "+(System.currentTimeMillis()-time)+"秒");
//        return obj;
//    }
//    public static void arountbefor(){
//        System.out.println("环绕前方法");
//    }
//    public static void arountafter(){
//        System.out.println("环绕后方法");
//    }
}
