package com.huang.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * @Time 2022-04-21 9:05
 * Created by Huang
 * className: CommonUtils
 * Description:
 */
public class CommonUtils {
    private static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    public static String cleanHtmlTag(String content) {
        if (StringUtils.isEmpty(content)) {
            return StringUtils.EMPTY;
        }
        return content.replaceAll(RE_HTML_MARK, StringUtils.EMPTY);
    }

    public static String getCurrentWeather(){
        String weatherUrl = "http://www.weather.com.cn/data/cityinfo/101200101.html";
        JSONObject result = HttpUtils.get(weatherUrl);
        JSONObject weatherInfo = result.getJSONObject("weatherinfo");
        String city = weatherInfo.getString("city");
        String lowTemp = weatherInfo.getString("temp1");
        String highTemp = weatherInfo.getString("temp2");
        String weather = weatherInfo.getString("weather");
        return city + " " + weather + " " + lowTemp + "~" + highTemp;
    }
}
