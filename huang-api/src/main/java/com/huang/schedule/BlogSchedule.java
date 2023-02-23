package com.huang.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huang.entity.SysStatisticsEntity;
import com.huang.mapper.*;
import com.huang.security.utils.RedisUtil;
import com.huang.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Time 2022-05-06 13:52
 * Created by Huang
 * className: BlogSchedule
 * Description:
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class BlogSchedule implements SchedulingConfigurer {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysStatisticsMapper sysStatisticsMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(asyncTaskThreadPool());
    }

    @Bean("asyncTaskThreadPool")
    public TaskExecutor asyncTaskThreadPool() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("Huang-Blog-AsyncTask-");//配置线程池前缀
        executor.setWaitForTasksToCompleteOnShutdown(true);//用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            log.warn("AsyncTask-当前任务线程池队列已满!");
        });//配置拒绝策略
        executor.initialize();//初始化线程池。
        return executor;
    }

    /***
     * @Description: 定时读取redis中每日访问数量 每分钟执行一次更新
     * @return: void
     **/
    @Async("asyncTaskThreadPool")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void collectVisitInfo() {
        log.info("start collectVisitInfo: {}", LocalDateTime.now());
        Integer visitCount = (Integer) redisUtil.get(Constant.VISIT_COUNT_PREFIX);
        Integer visitIpCount = Math.toIntExact(redisUtil.sScard(Constant.VISIT_IP));
        QueryWrapper<SysStatisticsEntity> statisticsWrapper = new QueryWrapper<>();
        statisticsWrapper.orderByDesc("create_time");
        SysStatisticsEntity sysStatisticsEntity = sysStatisticsMapper.selectList(statisticsWrapper).stream().findFirst().orElse(null);
        if(sysStatisticsEntity == null){
            sysStatisticsEntity = new SysStatisticsEntity();
        }
        Integer visit = sysStatisticsEntity.getVisit() == null?0:sysStatisticsEntity.getVisit();
        Integer ipVisit = sysStatisticsEntity.getIpVisit() == null?0:sysStatisticsEntity.getIpVisit();
        boolean visitEquals = !visit.equals(visitCount);
        if(visitEquals){
            sysStatisticsEntity.setVisit(visitCount);
        }
        boolean ipVisitEquals = !ipVisit.equals(visitIpCount);
        if(ipVisitEquals){
            sysStatisticsEntity.setIpVisit(visitIpCount);
        }
        if (visitEquals || ipVisitEquals) {
            //TODO likes
            sysStatisticsEntity.setLikes(0);
            sysStatisticsMapper.updateById(sysStatisticsEntity);
        }
        log.info("stop collectVisitInfo: {}", LocalDateTime.now());
    }

    /***
     * @Description: 定时读取redis中每日访问数量 每分钟执行一次更新
     * @return: void
     **/
    @Async("asyncTaskThreadPool")
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void addNewStatisticsRecord() {
        log.info("start addNewStatisticsRecord: {}", LocalDateTime.now());
        redisUtil.del(Constant.VISIT_COUNT_PREFIX,Constant.VISIT_IP);
        QueryWrapper<SysStatisticsEntity> statisticsWrapper = new QueryWrapper<>();
        statisticsWrapper.orderByDesc("create_time");
        SysStatisticsEntity sysStatisticsEntity = sysStatisticsMapper.selectList(statisticsWrapper).stream().findFirst().orElse(null);
        if(sysStatisticsEntity == null){
            sysStatisticsEntity = new SysStatisticsEntity();
        }
        SysStatisticsEntity entity = new SysStatisticsEntity();
        entity.setLikes(0);
        entity.setVisit(0);
        entity.setIpVisit(0);
        entity.setPostCount(sysStatisticsEntity.getPostCount());
        entity.setJournalCount(sysStatisticsEntity.getJournalCount());
        entity.setCategoryCount(sysStatisticsEntity.getCategoryCount());
        entity.setTagCount(sysStatisticsEntity.getTagCount());
        entity.setBirthday(sysStatisticsEntity.getBirthday());
        sysStatisticsMapper.insert(entity);
        log.info("stop addNewStatisticsRecord: {}", LocalDateTime.now());
    }
}
