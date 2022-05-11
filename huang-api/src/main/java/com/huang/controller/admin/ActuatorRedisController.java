package com.huang.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.huang.entity.RedisInfo;
import com.huang.service.RedisService;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/redis")
public class ActuatorRedisController {
    @Autowired
    private RedisService redisService;

    @GetMapping("/info")
    public R getRedisInfo() throws Exception {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        return R.ok().put("data",infoList);
    }

    @GetMapping("/keysSize")
    public Map<String, Object> getKeysSize() throws Exception {
        return redisService.getKeysSize();
    }

    @GetMapping("/keysSizeForReport")
    public Map<String, JSONArray> getKeysSizeReport() throws Exception {
		return redisService.getMapForReport("1");
    }

    @GetMapping("/memoryForReport")
    public Map<String, JSONArray> memoryForReport() throws Exception {
		return redisService.getMapForReport("2");
    }

    @GetMapping("/infoForReport")
    public Map<String, JSONArray> infoForReport() throws Exception {
		return redisService.getMapForReport("3");
    }

    @GetMapping("/memoryInfo")
    public Map<String, Object> getMemoryInfo() throws Exception {
        return redisService.getMemoryInfo();
    }
    
  	@GetMapping("/queryDiskInfo")
  	public R queryDiskInfo(HttpServletRequest request, HttpServletResponse response){
		List<Map<String,Object>> list = new ArrayList<>();
		try {
  			// 当前文件系统类
  	        FileSystemView fsv = FileSystemView.getFileSystemView();
  	        // 列出所有windows 磁盘
  	        File[] fs = File.listRoots();
  	        for (int i = 0; i < fs.length; i++) {
  	        	if(fs[i].getTotalSpace()==0) {
  	        		continue;
  	        	}
  	        	Map<String,Object> map = new HashMap<>();
  	        	map.put("name", fsv.getSystemDisplayName(fs[i]));
  	        	map.put("max", fs[i].getTotalSpace());
  	        	map.put("rest", fs[i].getFreeSpace());
  	        	map.put("restPPT", (fs[i].getTotalSpace()-fs[i].getFreeSpace())*100/fs[i].getTotalSpace());
  	        	list.add(map);
  	        }
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return R.ok().put("data",list);
  	}
}
