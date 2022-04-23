package com.huang.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

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

    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if(a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}
