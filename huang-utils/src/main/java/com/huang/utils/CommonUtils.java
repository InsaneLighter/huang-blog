package com.huang.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @Time 2022-04-21 9:05
 * Created by Huang
 * className: CommonUtils
 * Description:
 */
public class CommonUtils {
    private static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
    private static final String UNKNOWN = "unknown";
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    private final static Ip2regionSearcher IP_SEARCHER = SpringContextHolder.getBean(Ip2regionSearcher.class);
    private static final UserAgentAnalyzer USER_AGENT_ANALYZER = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .withField(UserAgent.AGENT_NAME_VERSION)
            .build();

    public static final Map<String, String> WEATHER_MAP = new HashMap() {{
        put("CLEAR_DAY", "晴（白天）");
        put("CLEAR_NIGHT", "晴（夜间）");
        put("PARTLY_CLOUDY_DAY", "多云（白天）");
        put("PARTLY_CLOUDY_NIGHT", "多云（夜间）");
        put("CLOUDY", "阴天");
        put("LIGHT_HAZE", "轻度雾霾");
        put("MODERATE_HAZE", "中度雾霾");
        put("HEAVY_HAZE", "重度雾霾");
        put("LIGHT_RAIN", "小雨");
        put("MODERATE_RAIN", "中雨");
        put("HEAVY_RAIN", "大雨");
        put("STORM_RAIN", "暴雨");
        put("FOG", "雾天");
        put("LIGHT_SNOW", "小雪");
        put("MODERATE_SNOW", "中雪");
        put("HEAVY_SNOW", "大雪");
        put("STORM_SNOW", "暴雪");
        put("DUST", "浮尘");
        put("SAND", "沙尘");
        put("WIND", "大风");
    }};


    public static String cleanHtmlTag(String content) {
        if (StringUtils.isEmpty(content)) {
            return StringUtils.EMPTY;
        }
        return content.replaceAll(RE_HTML_MARK, StringUtils.EMPTY);
    }


    public static String getCurrentWeather(String cityNum) {
        String weatherUrl = "http://www.weather.com.cn/data/cityinfo/" + cityNum + ".html";
        JSONObject result = HttpUtils.get(weatherUrl);
        JSONObject weatherInfo = result.getJSONObject("weatherinfo");
        String city = weatherInfo.getString("city");
        String lowTemp = weatherInfo.getString("temp1");
        String highTemp = weatherInfo.getString("temp2");
        String weather = weatherInfo.getString("weather");

        return city + " " + weather + " " + lowTemp + "~" + highTemp;
    }

    public static String getCurrentWeather() {
        String weatherUrl = "https://api.caiyunapp.com/v2.6/TAkhjf8d1nlSlspN/113.83727,30.66301/realtime";
        JSONObject result = HttpUtils.get(weatherUrl);
        String status = result.getString("status");
        if("ok".equals(status)){
            JSONObject weatherInfo = result.getJSONObject("result").getJSONObject("realtime");
            String temperature = weatherInfo.getString("temperature");
            String skycon = weatherInfo.getString("skycon");
            return "武汉" + "  " + WEATHER_MAP.get(skycon) + "  " + temperature + "℃";
        }
        return "武汉 天气未知";
    }

    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    public static String getString(Object object) {
        if (isEmpty(object)) {
            return "";
        }
        return (object.toString().trim());
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return (true);
        }
        if ("".equals(object)) {
            return (true);
        }
        if ("null".equals(object)) {
            return (true);
        }
        return (false);
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        return getLocalCityInfo(ip);
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(IP_URL, ip);
        cn.hutool.json.JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getLocalCityInfo(String ip) {
        IpInfo ipInfo = IP_SEARCHER.memorySearch(ip);
        if (ipInfo != null) {
            return ipInfo.getAddress();
        }
        return null;

    }

    public static String getBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent userAgent = USER_AGENT_ANALYZER.parse(request.getHeader("User-Agent"));
        return userAgent.get(UserAgent.AGENT_NAME_VERSION).getValue();
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

}
