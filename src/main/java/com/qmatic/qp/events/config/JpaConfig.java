/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.qmatic.qp.events.jpa")
@PropertySource("classpath:db.properties")
public class JpaConfig {
	
	@Bean
	DataSource dataSource(Environment env) {
		JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		dsLookup.setResourceRef(true);
		
		DataSource ds = dsLookup.getDataSource(env.getRequiredProperty("datasource.jndi-name"));
		return ds;
	}
	
	@Bean
	EntityManagerFactory entityManagerFactory(DataSource dataSource, Environment env) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.qmatic.qp.events.jpa.model");
        factory.setDataSource(dataSource);
        
        final Properties props = new Properties();
        props.put("hibernate.cache.use_second_level_cache", env.getRequiredProperty("hibernate.cache.use_second_level_cache"));
        props.put("hibernate.cache.region.factory_class", env.getRequiredProperty("hibernate.cache.region.factory_class"));
        props.put("hibernate.cache.use_query_cache", env.getRequiredProperty("hibernate.cache.use_query_cache"));
        props.put("hibernate.generate_statistics", env.getRequiredProperty("hibernate.generate_statistics"));
        props.put("hibernate.transaction.jta.platform", env.getRequiredProperty("hibernate.transaction.jta.platform"));
        props.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        
        factory.setJpaProperties(props);
        
        factory.afterPropertiesSet();

        return factory.getObject();
	}
	
	@Bean
	PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		txManager.setJpaDialect(new HibernateJpaDialect());
		return txManager;
	}
}
