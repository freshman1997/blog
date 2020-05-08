package com.yuan.cn.blog.config.normal;

import com.github.pagehelper.PageInterceptor;
import com.yuan.cn.blog.annotation.Mapper;
import com.yuan.cn.blog.utils.PropertiesUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class MybatisConfig {

    private DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(PropertiesUtil.getProperty("db.driverClassName"));
        dataSource.setUrl(PropertiesUtil.getProperty("db.url"));
        dataSource.setUsername(PropertiesUtil.getProperty("db.username"));
        dataSource.setPassword(PropertiesUtil.getProperty("db.password"));
        dataSource.setValidationQuery("select 1");

        dataSource.setInitialSize(Integer.parseInt(PropertiesUtil.getProperty("db.initialSize")));
        dataSource.setMaxActive(Integer.parseInt(PropertiesUtil.getProperty("db.maxActive")));
        dataSource.setMinIdle(Integer.parseInt(PropertiesUtil.getProperty("db.minIdle")));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(PropertiesUtil.getProperty("db.minEvictableIdleTimeMillis")));
        dataSource.setMaxIdle(Integer.parseInt(PropertiesUtil.getProperty("db.maxIdle")));
        dataSource.setDefaultAutoCommit(Boolean.parseBoolean(PropertiesUtil.getProperty("db.defaultAutoCommit")));
        dataSource.setMaxWait(Long.parseLong(PropertiesUtil.getProperty("db.maxWait")));
        return dataSource;

    }

    private DataSource dataSource = dataSource();

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() {

        ClassPathResource userMapperXML = new ClassPathResource("/mappers/UserMapper.xml");
        ClassPathResource blogMapperXML = new ClassPathResource("/mappers/BlogMapper.xml");
        ClassPathResource loveMapperXML = new ClassPathResource("/mappers/LoveMapper.xml");
        ClassPathResource categoryMapperXML = new ClassPathResource("/mappers/CategoryMapper.xml");
        ClassPathResource commentMapperXML = new ClassPathResource("/mappers/CommentMapper.xml");
        ClassPathResource tagMapperXML = new ClassPathResource("/mappers/TagMapper.xml");
        ClassPathResource tagCategoryMapperXML = new ClassPathResource("/mappers/TagCategoryMapper.xml");
        ClassPathResource blogReadMapperXML = new ClassPathResource("/mappers/BlogReadMapper.xml");

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new Resource[]{userMapperXML, blogMapperXML,
                loveMapperXML, categoryMapperXML, commentMapperXML, tagCategoryMapperXML, tagMapperXML, blogReadMapperXML});

        sqlSessionFactoryBean.setPlugins(new Interceptor[]{initPageInterceptor()});
        return sqlSessionFactoryBean;
    }

    private PageInterceptor initPageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.yuan.cn.blog");
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setRollbackOnCommitFailure(true);
        return transactionManager;
    }
}
