package com.example.aop;



import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    // 공통적인 작업을 구현하기 위한 용도
    // 패키지가 com.example.controller인 컨트롤러는 모두 수행
    @Around("execution(* com.example.controller.*Controller.*(..)) or execution(* com.example.mapper.*Mapper.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable{

        // 현재시간 보관
        long start = System.currentTimeMillis();

        // 수행되는 클래스명
        String className = joinPoint.getSignature().getDeclaringTypeName();

        String type = "";
        if (className.contains("Controller") == true ) {
            type = "Controller => ";
        }
        else if (className.contains("Mapper") == true) {
            type = "Mapper => ";
        }
        else if (className.contains("Service") == true) {
            type = "Service => ";
        }
        // 끝나는 시간
        long end = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();

        logger.info(type + className + "->" + methodName);
        logger.info("execute time : " + (end-start));
        // System.out.println("클래스명: "+ className + "메소드명: "+ methodName);

        return joinPoint.proceed();
    }

    /*
    // 특정 컨트롤러의 메소드가 수행될 시 현재의 주소를 저장하는 용도
    @Around("execution(* com.example.controller.*Controller.*(..))")
    public Object sessionLog(ProceedingJoinPoint joinPoint) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession httpSession = request.getSession();

        // 수행되는 클래스명
        String className = joinPoint.getSignature().getDeclaringTypeName();

        // 수행되는 메소드명
        String methodName = joinPoint.getSignature().getName();

        // 127.0.0.1:9090/ROOT/home?abc=3
        // 수행되는 주소명
        String pathName = request.getServletPath();
        // 수행되는 QUERY명
        String queryName = request.getQueryString();

        System.out.println("-------------------------");
        System.out.println(className + "," + methodName 
				+ "," + pathName+","+queryName);

        if (!pathName.startsWith("/member/login") && !pathName.startsWith("/member/logout")) {
            httpSession.setAttribute("BACKURL", httpSession);
        }
        return joinPoint.proceed();
    }
    */
}
