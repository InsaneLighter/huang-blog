package com.huang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.huang.entity.RedisInfo;
import com.huang.service.RedisService;
import com.huang.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Redis 监控信息获取
 *
 * @Author MrBird
 */
@Service("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {

	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * Redis详细信息
	 */
	@Override
	public List<RedisInfo> getRedisInfo() {
		Properties info = redisConnectionFactory.getConnection().info();
		List<RedisInfo> infoList = new ArrayList<>();
		RedisInfo redisInfo = null;
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			redisInfo = new RedisInfo();
			redisInfo.setKey(CommonUtils.getString(entry.getKey()));
			redisInfo.setValue(CommonUtils.getString(entry.getValue()));
			infoList.add(redisInfo);
		}
		return infoList;
	}

	@Override
	public Map<String, Object> getKeysSize() {
		Long dbSize = redisConnectionFactory.getConnection().dbSize();
		Map<String, Object> map = new HashMap<>();
		map.put("create_time", System.currentTimeMillis());
		map.put("dbSize", dbSize);
		return map;
	}

	@Override
	public Map<String, Object> getMemoryInfo() {
		Map<String, Object> map = null;
		Properties info = redisConnectionFactory.getConnection().info();
		assert info != null;
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			String key = CommonUtils.getString(entry.getKey());
			if ("used_memory".equals(key)) {
				map = new HashMap<>();
				map.put("used_memory", entry.getValue());
				map.put("create_time", System.currentTimeMillis());
			}
		}
		return map;
	}

    /**
     * 查询redis信息for报表
     * @param type 1redis key数量 2 占用内存 3redis信息
     * @return
     */
	@Override
	public Map<String, JSONArray> getMapForReport(String type)  {
		Map<String,JSONArray> mapJson=new HashMap<String, JSONArray> ();
		JSONArray json = new JSONArray();
		if("3".equals(type)){
			List<RedisInfo> redisInfo = getRedisInfo();
			for(RedisInfo info:redisInfo){
				Map<String, Object> map= Maps.newHashMap();
				BeanMap beanMap = BeanMap.create(info);
				for (Object key : beanMap.keySet()) {
					map.put(key+"", beanMap.get(key));
				}
				json.add(map);
			}
			mapJson.put("data",json);
			return mapJson;
		}
		for(int i = 0; i < 5; i++){
			JSONObject jo = new JSONObject();
			Map<String, Object> map;
			if("1".equals(type)){
				map= getKeysSize();
				jo.put("value",map.get("dbSize"));
			}else{
				map = getMemoryInfo();
				int used_memory = Integer.parseInt(map.get("used_memory").toString());
				jo.put("value",used_memory/1000);
			}
			String create_time = DateUtil.formatTime(DateUtil.date((Long) map.get("create_time")-(4-i)*1000));
			jo.put("name",create_time);
			json.add(jo);
		}
		mapJson.put("data",json);
		return mapJson;
	}
}
