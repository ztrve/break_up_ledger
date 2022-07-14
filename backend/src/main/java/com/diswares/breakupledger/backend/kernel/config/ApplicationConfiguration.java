package com.diswares.breakupledger.backend.kernel.config;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import com.diswares.breakupledger.backend.kernel.magic.design.ScopedContext;
import com.diswares.breakupledger.backend.kernel.magic.interceptor.DataScopesInterceptor;
import com.diswares.breakupledger.backend.kernel.plugins.SpreadPlugin;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.sql.DataSource;

/**
 * @author 4everlynn
 * @version V1.0
 * @date 2021/8/2
 */
@Configuration
@Slf4j
@AllArgsConstructor
public class ApplicationConfiguration {

    private final ApplicationContext applicationContext;

    @Bean
    MybatisPlusInterceptor interceptor() {
        final MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        pageInterceptor.setOptimizeJoin(false);
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder
                .featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }

    public DataScopesInterceptor dataScopesInterceptor() {
        ScopedContext context = applicationContext.getBean(ScopedContext.class);
        return new DataScopesInterceptor().scopes(context);
    }

    @Bean
    public SqlSessionFactory
    sqlSessionFactory(DataSource dataSource,
                      MybatisPlusInterceptor plusInterceptor,
                      MybatisPlusProperties mybatisProperties) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPlugins(plusInterceptor);
        sessionFactory.setConfiguration(mybatisProperties.getConfiguration());

        try {
            final DataScopesInterceptor interceptor = dataScopesInterceptor();
            sessionFactory.getConfiguration().addInterceptor(interceptor);
            log.info("DataScope Interceptor enabled successfully.");
        } catch (Exception ignored) {
            log.info("ScopedContext or JdbcTemplate not found, abandon injection data scope interceptor.");
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 需要在这里指定xml文件的位置，不然自定义的sql会报 Invalid bound statement 异常
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
        return sessionFactory.getObject();
    }

    @ConditionalOnMissingBean(SpreadPlugin.class)
    @Bean
    public SpreadPlugin spreadPlugin(){
        return new SpreadPlugin();
    }
}
