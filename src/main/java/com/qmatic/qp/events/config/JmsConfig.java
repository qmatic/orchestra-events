/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.config;

import javax.jms.TopicConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.ErrorHandler;

import com.qmatic.qp.events.jms.EventErrorHandler;

@Configuration
@EnableJms
@ComponentScan(basePackages = "com.qmatic.qp.events.jms")
public class JmsConfig {

	@Bean
	JndiObjectFactoryBean jndiJmsTopicConnectionFactory() {
		JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
		jndiFactory.setJndiName("QPInternalConnectionFactory");
		jndiFactory.setLookupOnStartup(false);
		jndiFactory.setCache(true);
		jndiFactory.setProxyInterface(TopicConnectionFactory.class);
		
		return jndiFactory;
	}
	
	@Bean
	SingleConnectionFactory jmsTopicConnectionFactory() {
		SingleConnectionFactory connectionFactory = new SingleConnectionFactory();
		connectionFactory.setTargetConnectionFactory((TopicConnectionFactory) jndiJmsTopicConnectionFactory().getObject());
		
		return connectionFactory;
	}
	
	@Bean
	DestinationResolver destinationResolver() {
		return new JndiDestinationResolver();
	}
	
	@Bean
	ErrorHandler errorHandler() {
		return new EventErrorHandler();
	}
	
	@Bean
	DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(jmsTopicConnectionFactory());
		factory.setDestinationResolver(destinationResolver());
		factory.setErrorHandler(errorHandler());
		factory.setPubSubDomain(true);
		
		return factory;
	}
}
