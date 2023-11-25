# Brave's flushOnUpdate() span problem with Spring Cloud Sleuth JDBC

This repo provides a minimum working example showing how adding a brave field
with `flushOnUpdate()` causes misalignment error when both `@NewSpan` and
`@Transactional` annotations are used on a bean method.

```
java.lang.AssertionError: Misalignment: popped updateScope false !=  expected false
    at brave.baggage.CorrelationFlushScope.popCurrentUpdateScope(CorrelationFlushScope.java:86) ~[brave-5.13.9.jar:na]
    at brave.baggage.CorrelationFlushScope.close(CorrelationFlushScope.java:37) ~[brave-5.13.9.jar:na]
    at org.springframework.cloud.sleuth.brave.bridge.BraveScope.close(BraveCurrentTraceContext.java:136) ~[spring-cloud-sleuth-brave-3.1.9.jar:3.1.9]
    at org.springframework.cloud.sleuth.brave.bridge.RevertingScope.close(BraveCurrentTraceContext.java:120) ~[spring-cloud-sleuth-brave-3.1.9.jar:3.1.9]
    at org.springframework.cloud.sleuth.brave.bridge.BraveSpanInScope.close(BraveTracer.java:158) ~[spring-cloud-sleuth-brave-3.1.9.jar:3.1.9]
    at org.springframework.cloud.sleuth.SpanAndScope.close(SpanAndScope.java:63) ~[spring-cloud-sleuth-api-3.1.9.jar:3.1.9]
    at org.springframework.cloud.sleuth.instrument.jdbc.TraceListenerStrategy.afterConnectionClose(TraceListenerStrategy.java:439) ~[spring-cloud-sleuth-instrumentation-3.1.9.jar:3.1.9]
    at org.springframework.cloud.sleuth.instrument.jdbc.TraceJdbcEventListener.onAfterConnectionClose(TraceJdbcEventListener.java:145) ~[spring-cloud-sleuth-instrumentation-3.1.9.jar:3.1.9]
    at com.p6spy.engine.event.CompoundJdbcEventListener.onAfterConnectionClose(CompoundJdbcEventListener.java:292) ~[p6spy-3.9.1.jar:na]
    at com.p6spy.engine.wrapper.ConnectionWrapper.close(ConnectionWrapper.java:242) ~[p6spy-3.9.1.jar:na]
    at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.closeConnection(DatasourceConnectionProviderImpl.java:127) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.internal.NonContextualJdbcConnectionAccess.releaseConnection(NonContextualJdbcConnectionAccess.java:49) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.releaseConnection(LogicalConnectionManagedImpl.java:219) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.close(LogicalConnectionManagedImpl.java:261) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl.close(JdbcCoordinatorImpl.java:175) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.internal.AbstractSharedSessionContract.close(AbstractSharedSessionContract.java:374) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.internal.SessionImpl.closeWithoutOpenChecks(SessionImpl.java:413) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at org.hibernate.internal.SessionImpl.close(SessionImpl.java:398) ~[hibernate-core-5.6.15.Final.jar:5.6.15.Final]
    at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
    at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
    at org.springframework.orm.jpa.ExtendedEntityManagerCreator$ExtendedEntityManagerInvocationHandler.invoke(ExtendedEntityManagerCreator.java:362) ~[spring-orm-5.3.30.jar:5.3.30]
    at jdk.proxy2/jdk.proxy2.$Proxy135.close(Unknown Source) ~[na:na]
    at org.springframework.orm.jpa.EntityManagerFactoryUtils.closeEntityManager(EntityManagerFactoryUtils.java:427) ~[spring-orm-5.3.30.jar:5.3.30]
    at org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor.afterCompletion(OpenEntityManagerInViewInterceptor.java:112) ~[spring-orm-5.3.30.jar:5.3.30]
    at org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter.afterCompletion(WebRequestHandlerInterceptorAdapter.java:73) ~[spring-webmvc-5.3.30.jar:5.3.30]
    at org.springframework.web.servlet.HandlerExecutionChain.triggerAfterCompletion(HandlerExecutionChain.java:178) ~[spring-webmvc-5.3.30.jar:5.3.30]
    at org.springframework.web.servlet.DispatcherServlet.processDispatchResult(DispatcherServlet.java:1168) ~[spring-webmvc-5.3.30.jar:5.3.30]
    at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089) ~[spring-webmvc-5.3.30.jar:5.3.30]
    at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965) ~[spring-webmvc-5.3.30.jar:5.3.30]
    ...
``` 

Run `TextControllerTest` to observe the error and see `CorrelationScopeCustomizer` bean which causes it.
