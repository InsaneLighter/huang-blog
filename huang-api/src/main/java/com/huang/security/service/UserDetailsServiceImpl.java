package com.huang.security.service;

import com.huang.entity.SysUserEntity;
import com.huang.entity.bean.LoginProperties;
import com.huang.entity.dto.JwtUserDto;
import com.huang.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService sysUserService;
    private final LoginProperties loginProperties;

    private final UserCacheManager CACHE_MANAGER;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    public static ExecutorService executor = newThreadPool();

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = new JwtUserDto();
        Future<JwtUserDto> future = CACHE_MANAGER.get(username);
        if (!loginProperties.isCacheEnable()) {
            SysUserEntity user;
            try {
                user = sysUserService.findByName(username);
                jwtUserDto.setUser(user);
            } catch (UsernameNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException(username, e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在！");
            }
            return jwtUserDto;
        }

        if (future == null) {
            Callable<JwtUserDto> call = () -> getJwtBySearchDb(username);
            FutureTask<JwtUserDto> ft = new FutureTask<>(call);
            future = CACHE_MANAGER.putIfAbsent(username, ft);
            if (future == null) {
                future = ft;
                executor.submit(ft);
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                CACHE_MANAGER.remove(username);
                System.out.println("error" + Thread.currentThread().getName());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                jwtUserDto = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return jwtUserDto;

    }

    private JwtUserDto getJwtBySearchDb(String username) {
        SysUserEntity user;
        JwtUserDto jwtUserDto = new JwtUserDto();
        try {
            user = sysUserService.findByName(username);
            jwtUserDto.setUser(user);
        } catch (UsernameNotFoundException e) {
            // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
            throw new UsernameNotFoundException("", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return jwtUserDto;
    }

    public static ExecutorService newThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactory() {
            final AtomicInteger sequence = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                int seq = this.sequence.getAndIncrement();
                thread.setName("future-task-thread" + (seq > 1 ? "-" + seq : ""));
                if (!thread.isDaemon()) {
                    thread.setDaemon(true);
                }

                return thread;
            }
        };
        return new ThreadPoolExecutor(10, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
