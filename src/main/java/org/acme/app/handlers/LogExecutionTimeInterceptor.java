package org.acme.app.handlers;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.acme.app.service.sqs.SQSService;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.LocalTime;

@Interceptor
@LogExecutionTime
public class LogExecutionTimeInterceptor {
    public static final Logger LOG = Logger.getLogger(LogExecutionTimeInterceptor.class);

    @Inject
    SQSService sqsService;

    @AroundInvoke
    public Object logExecutionTime(InvocationContext ctx) throws Exception {
        LocalTime startTime = LocalTime.now();
        try {

            return ctx.proceed();
        }finally {
            LocalTime endTime = LocalTime.now();
            sqsService.LOG(ctx.getMethod().getName()+" "+Duration.between(startTime, endTime).toMillis()+"ms");
        }
    }
}
