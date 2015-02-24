/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.comet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;

import org.cometd.annotation.ServerAnnotationProcessor;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.BayeuxServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Spring component to configure cometD
 * 
 * @author gavsmi
 *
 */
@Component
@Singleton
public class CometConfigurer implements DestructionAwareBeanPostProcessor, ServletContextAware {

	private static final Logger log = LoggerFactory.getLogger(CometConfigurer.class);
	
	private BayeuxServer bayeuxServer;
    private ServerAnnotationProcessor processor;
    
    @Inject
    public void setBayeuxServer(BayeuxServer bayeuxServer) {
    	this.bayeuxServer = bayeuxServer;
    }
    
    @PostConstruct
    public void init() {
    	this.processor = new ServerAnnotationProcessor(bayeuxServer);
    }
    
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
    	log.debug("Configuring service {}", name);
    	
    	processor.processDependencies(bean);
    	processor.processConfigurations(bean);
    	processor.processCallbacks(bean);
    	return bean;
    }
    
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        return bean;
    }
 
    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
        processor.deprocessCallbacks(bean);
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public BayeuxServer bayeuxServer() {
    	BayeuxServerImpl bean = new BayeuxServerImpl();
        bean.setOption(BayeuxServerImpl.LOG_LEVEL, "1");
        return bean;
    }
    
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(BayeuxServer.ATTRIBUTE, bayeuxServer);
    }
}
