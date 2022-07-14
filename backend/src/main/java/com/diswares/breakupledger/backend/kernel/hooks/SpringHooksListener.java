package com.diswares.breakupledger.backend.kernel.hooks;

import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.base.Joiner;
import com.diswares.breakupledger.backend.kernel.genius.GeniusHooks;
import com.diswares.breakupledger.backend.kernel.genius.GeniusProxyWrapper;
import com.diswares.breakupledger.backend.kernel.genius.annotions.Export;
import com.diswares.breakupledger.backend.kernel.utils.ProxyUtils;
import com.diswares.breakupledger.backend.kernel.utils.QuickAccess;
import com.diswares.breakupledger.backend.kernel.vo.AncestorDomain;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The type Spring hooks listener.
 *
 * @author 4everlynn
 * @version V1.0
 * @date 2021 /7/30
 */
@SuppressWarnings("unchecked")
@Slf4j
@Service
public class SpringHooksListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 监听项目启动的钩子
     *
     * @param contextRefreshedEvent contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Service started");
        final ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        // 注册动态路由
        QuickAccess.run(() -> this.registerHandler(applicationContext));
    }

    @SneakyThrows
    private void registerHandler(ApplicationContext applicationContext) {

        final RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Export.class);

        for (Object value : beans.values()) {
            final Object target = ProxyUtils.getTarget(value);
            if (target instanceof Proxy) {
                continue;
            }

            final Type superclass = target.getClass().getGenericSuperclass();
            final Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
            Class<? extends AncestorDomain> domain = null;

            if (null != actualTypeArguments) {
                for (Type actualTypeArgument : actualTypeArguments) {
                    final Class<?> domainClazz = Class.forName(actualTypeArgument.getTypeName());
                    if (AncestorDomain.class.isAssignableFrom(domainClazz)) {
                        domain = (Class<? extends AncestorDomain>) domainClazz;
                        break;
                    }
                }
            }

            final Class<?> tClazz = target.getClass();
            Class<?> clazz = null;

            final AnnotatedType[] annotatedInterfaces = tClazz.getAnnotatedInterfaces();
            for (AnnotatedType annotatedInterface : annotatedInterfaces) {
                final Type type = annotatedInterface.getType();
                final Class<?> aClass = Class.forName(type.getTypeName());
                if (null != aClass.getDeclaredAnnotation(Export.class)) {
                    clazz = aClass;
                    break;
                }
            }

            if (null != clazz && IService.class.isAssignableFrom(clazz)) {
                final Export annotation = clazz.getDeclaredAnnotation(Export.class);

                GeniusHooks<?> hooks = this.getGeniusHooks(domain, applicationContext);

                final GeniusProxyWrapper geniusProxyWrapper
                        = new GeniusProxyWrapper((Class<? extends IService<? extends AncestorDomain>>) clazz, domain, hooks);

                // 上下文对象注入
                geniusProxyWrapper.context(applicationContext);

                // 拼接路径
                BiFunction<String, String[], String> generate = (base, path) -> {
                    if (path.length > 0) {
                        return Joiner.on("").join(base, "/",
                                Arrays.stream(path)
                                        .reduce((l, r) -> l.concat("/").concat(r)
                                                .replaceAll("/$", ""))
                                        .map(it -> it
                                                .replaceAll("/$", "")
                                                .replaceAll("^/", ""))
                                        .orElse(""));
                    }
                    return base;
                };

                this.registerRoutes(geniusProxyWrapper, annotation, generate, handlerMapping);

            }
        }
    }

    @SneakyThrows
    @SuppressWarnings("rawtypes")
    private GeniusHooks<?> getGeniusHooks(Class<? extends AncestorDomain> domain, ApplicationContext applicationContext) {
        final Map<String, GeniusHooks> hooksMap = applicationContext.getBeansOfType(GeniusHooks.class);

        for (GeniusHooks value : hooksMap.values()) {
            final Type[] types = value.getClass().getGenericInterfaces();
            for (Type type : types) {
                final Field field = type.getClass().getDeclaredField("actualTypeArguments");
                field.setAccessible(true);
                final Type[] genericParadigms = (Type[]) field.get(type);
                for (Type genericParadigm : genericParadigms) {
                    final Class<?> typeClazz = Class.forName(genericParadigm.getTypeName());
                    if (domain.isAssignableFrom(typeClazz)) {
                        log.info("{} inject hooks.", domain.getSimpleName());
                        if (value.transactional()) {
                            log.info("{} hooks transactional enabled.", domain.getSimpleName());
                        }
                        return value;
                    }
                }
            }
        }

        return null;
    }

    private void registerRoutes(GeniusProxyWrapper geniusProxyWrapper, Export annotation,
                                BiFunction<String, String[], String> generate,
                                RequestMappingHandlerMapping handlerMapping) {

        for (Method method : geniusProxyWrapper.getClass().getMethods()) {

            // 标注了以下请求注解的方法，将会被定义为接口
            final GetMapping get = method.getDeclaredAnnotation(GetMapping.class);
            final PostMapping post = method.getDeclaredAnnotation(PostMapping.class);
            final DeleteMapping delete = method.getDeclaredAnnotation(DeleteMapping.class);
            final PatchMapping patch = method.getDeclaredAnnotation(PatchMapping.class);

            StringBuilder basePath = new StringBuilder()
                    .append(annotation.path().replaceAll("/$", ""));

            RequestMappingInfo mappingInfo = null;

            final boolean isGet = null != get && (annotation.page() || annotation.getFromId());

            if (isGet) {
                mappingInfo = RequestMappingInfo
                        .paths(generate.apply(basePath.toString(), get.value()))
                        .methods(RequestMethod.GET)
                        .build();
            }


            if (null != delete && annotation.delete()) {
                mappingInfo = RequestMappingInfo
                        .paths(generate.apply(basePath.toString(), delete.value()))
                        .methods(RequestMethod.DELETE)
                        .build();
            }

            if (null != patch && annotation.update()) {
                mappingInfo = RequestMappingInfo
                        .paths(generate.apply(basePath.toString(), patch.value()))
                        .methods(RequestMethod.PATCH)
                        .build();
            }

            if (null != post && annotation.create()) {
                mappingInfo = RequestMappingInfo
                        .paths(generate.apply(basePath.toString(), post.value()))
                        .methods(RequestMethod.POST)
                        .build();
            }

            if (null != mappingInfo) {
                try {
                    handlerMapping.registerMapping(mappingInfo, geniusProxyWrapper, method);
                    log.info("Route {} exported with method -> {}",
                            mappingInfo.getPatternsCondition(), mappingInfo.getMethodsCondition());
                } catch (IllegalStateException e) {
                    // fallback
                    log.warn("Route {} -> {} fallback, Reason: uri has been defined",
                            mappingInfo.getPatternsCondition(),
                            mappingInfo.getMethodsCondition()
                    );
                }

            }
        }

    }
}
