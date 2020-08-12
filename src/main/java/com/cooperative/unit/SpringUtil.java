package com.cooperative.unit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SpringUtil {

    private static ServletRequestAttributes servletRequestAttributes;

    public static ServletRequestAttributes getServletRequest() {
        if (null == servletRequestAttributes) {
            servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        }
        return servletRequestAttributes;
    }

    /**
     * 获取的是本地的IP地址
     *
     * @return
     */
    public static String serviceIp() {
        String result = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            result = address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("------>调用 IpUtil.servicerIp，错误信息如下");
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地址
     *
     * @param domain
     * @return
     */
    public static String serviceIp(String domain) {
        String result = "";
        if (StringUtils.isNoneBlank(domain)) {
            try {
                InetAddress inetAddress = InetAddress.getByName(domain);
                result = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                log.error("------>调用 IpUtil.servicerIp，错误信息如下");
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 根据主机名返回其可能的所有Ip地址
     *
     * @param domain
     * @return
     */
    public static List<String> originalServiceIp(String domain) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isNoneBlank(domain)) {
            try {
                InetAddress[] addresses = InetAddress.getAllByName(domain);
                for (InetAddress addr : addresses) {
                    result.add(addr.getHostAddress());
                }
            } catch (UnknownHostException e) {
                log.error("------>调用 IpUtil.originalServicerIp，错误信息如下");
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * @return
     * @description: 如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个， 而是一串IP值，
     * 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取 X-Forwarded-For中第一个非unknown的有效IP字符串。
     */
    public static String clientIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getServletRequest().getRequest().getSession();
    }

    public static String getDeCodeName(String userName) {
        if (StringUtils.isNotBlank(userName)) {
            try {
                userName = new String(userName.getBytes("ISO8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return userName;
    }

    /**
     * 取得HttpRequest的简化函数.
     */
    public static HttpServletRequest getRequest() {
        return getServletRequest().getRequest();
    }

    /**
     * 取得HttpResponse的简化函数.
     */
    public static HttpServletResponse getResponse() {
        return getServletRequest().getResponse();
    }

    /**
     * 取得Request Parameter的简化方法.
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static void setAttribute(String name, Object o) {
        getRequest().setAttribute(name, o);
    }

    public static Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }

    /**
     * 判断是否AJAX请求
     *
     * @return 是否AJAX请求
     */
    public static boolean isAjaxRequest() {
        String x_requested_with = getRequest().getHeader("x-requested-with");
        return x_requested_with != null && x_requested_with.equalsIgnoreCase("XMLHttpRequest");
    }
}
