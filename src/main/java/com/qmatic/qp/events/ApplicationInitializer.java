/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.cometd.server.CometdServlet;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.qmatic.qp.events.config.ApplicationConfig;
import com.qmatic.qp.events.config.AsyncConfig;
import com.qmatic.qp.events.config.JmsConfig;
import com.qmatic.qp.events.config.JpaConfig;
import com.qmatic.qp.events.config.WebMvcConfig;
import com.qmatic.qp.events.config.WebSocketConfig;

public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(ApplicationConfig.class, AsyncConfig.class, JmsConfig.class, JpaConfig.class, WebMvcConfig.class, WebSocketConfig.class);
		ctx.setServletContext(container);
		
		container.setInitParameter("org.eclipse.jetty.server.context.ManagedAttributes", "org.cometd.bayeux");
		
		ServletRegistration.Dynamic cometd = container.addServlet("cometd", new CometdServlet());
		cometd.addMapping("/cometd/*");
		
		FilterRegistration.Dynamic crossOrigin = container.addFilter("cross-origin", new CrossOriginFilter());
		crossOrigin.setAsyncSupported(true);
		crossOrigin.addMappingForServletNames(EnumSet.of (DispatcherType.REQUEST), true, "cometd");
		
		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(ctx));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/hooks/*");
	}

}
