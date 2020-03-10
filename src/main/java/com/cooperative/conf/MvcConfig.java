package com.cooperative.conf;

import com.cooperative.entity.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MVC全局特性
 * 拦截器
 * 跨域访问配置
 * 格式化
 * 等等定制接口
 */
//@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionHandlerInterceptor()).addPathPatterns("/admin/**");
    }

    /**
     * 检查用户是否已经登录，如果未登录，重定向到登录页面
     */
    class SessionHandlerInterceptor implements HandlerInterceptor {
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("/login");
                return false;
            }
            return true;
        }

        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
            //controller方法处理完毕后调用此方法
        }

        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            //页面渲染完毕后调用此方法，通常用来清除某些资源
        }
    }

    /**
     * 跨域访问设置
     */
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("http://qiandu.com").allowedMethods("POST", "GET");
    }

    /**
     * 格式化
     */
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index.html").setViewName("/index.btl");
        registry.addRedirectViewController("/**/*.do", "/index.html");
    }
}
