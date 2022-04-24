package com.huang.entity;

import java.util.HashMap;
import java.util.Map;

public class RedisInfo {

	private static final Map<String, String> map = new HashMap<>();

	static {
		map.put("number_of_cached_scripts","缓存脚本数量");
		map.put("db0", "db0的key的数量,以及带有生存期的key的数,平均存活时间");
		map.put("redis_version","Redis服务器的版本");
		map.put("redis_git_sha1","GitSHA1");
		map.put("redis_git_dirty","Git脏标志");
		map.put("redis_build_id","构建ID");
		map.put("redis_mode","服务器的模式（“独立”、“哨兵”或“集群”）");
		map.put("os","托管Redis服务器的操作系统");
		map.put("arch_bits","架构（32或64位）");
		map.put("multiplexing_api","Redis使用的事件循环机制");
		map.put("atomicvar_api","Redis使用的AtomicvarAPI");
		map.put("gcc_version","用于编译Redis服务器的GCC编译器版本");
		map.put("process_id","服务器进程的PID");
		map.put("process_supervised","受监督的系统（“upstart”、“systemd”、“unknown”或“no”）");
		map.put("run_id","标识Redis服务器的随机值（由Sentinel和Cluster使用）");
		map.put("tcp_port","TCP/IP监听端口");
		map.put("server_time_usec","基于纪元的系统时间，精度为微秒");
		map.put("uptime_in_seconds","Redis服务器启动后的秒数");
		map.put("uptime_in_days","以天为单位表示的相同值");
		map.put("hz","服务器当前频率设置");
		map.put("configured_hz","服务器配置的频率设置");
		map.put("lru_clock","时钟每分钟递增一次，用于LRU管理");
		map.put("executable","服务器可执行文件的路径");
		map.put("config_file","配置文件的路径");
		map.put("io_threads_active","指示I/O线程是否处于活动状态的标志");
		map.put("shutdown_in_milliseconds","副本在完成关闭序列之前赶上复制的最长时间。该字段仅在关机期间出现。");
		map.put("connected_clients","客户端连接数（不包括来自副本的连接）");
		map.put("cluster_connections","集群总线使用的套接字数量的近似值");
		map.put("maxclients","maxclients配置指令的值。connected_clients这是和之connected_slaves和的上限cluster_connections。");
		map.put("client_recent_max_input_buffer","当前客户端连接中最大的输入缓冲区");
		map.put("client_recent_max_output_buffer","当前客户端连接中最大的输出缓冲区");
		map.put("blocked_clients","等待阻塞调用的客户端数量(BLPOP,BRPOP,BRPOPLPUSH,BLMOVE,BZPOPMIN,BZPOPMAX)");
		map.put("tracking_clients","被跟踪的客户数量(CLIENTTRACKING)");
		map.put("clients_in_timeout_table","客户端超时表中的客户端数量");
		map.put("used_memory","Redis使用其分配器分配的总字节数（标准libc、jemalloc或替代分配器，例如tcmalloc）");
		map.put("used_memory_human","以前值的人类可读表示");
		map.put("used_memory_rss","操作系统看到的Redis分配的字节数（也称为驻留集大小）。这是由工具报告的数字，例如top(1)和ps(1)");
		map.put("used_memory_rss_human","以前值的人类可读表示");
		map.put("used_memory_peak","Redis消耗的峰值内存（以字节为单位）");
		map.put("used_memory_peak_human","以前值的人类可读表示");
		map.put("used_memory_peak_perc","used_memory_peak出来的百分比used_memory");
		map.put("used_memory_overhead","服务器分配用于管理其内部数据结构的所有开销的总和（以字节为单位）");
		map.put("used_memory_startup","Redis在启动时消耗的初始内存量（以字节为单位）");
		map.put("used_memory_dataset","数据集的大小（以字节为单位）（used_memory_overhead减去used_memory）");
		map.put("used_memory_dataset_perc","used_memory_dataset:超出净内存使用的百分比（used_memory减used_memory_startup）");
		map.put("total_system_memory","Redis主机拥有的内存总量");
		map.put("total_system_memory_human","以前值的人类可读表示");
		map.put("used_memory_lua","Lua引擎使用的字节数");
		map.put("used_memory_lua_human","以前值的人类可读表示");
		map.put("used_memory_scripts","缓存Lua脚本使用的字节数");
		map.put("used_memory_scripts_human","以前值的人类可读表示");
		map.put("maxmemory","maxmemory配置指令的值");
		map.put("maxmemory_human","以前值的人类可读表示");
		map.put("maxmemory_policy","maxmemory-policy配置指令的值");
		map.put("mem_fragmentation_ratio","used_memory_rss和之间的比率used_memory。请注意，这不仅包括碎片，还包括其他进程开销（参见allocator_*指标），以及代码、共享库、堆栈等开销。");
		map.put("mem_fragmentation_bytes","used_memory_rss和之间的差值used_memory。请注意，当总分片字节数较低（几兆字节）时，较高的比率（例如1.5及以上）并不表示存在问题。");
		map.put("allocator_frag_ratio","allocator_active和之间的比率allocator_allocated。这是真正的（外部）碎片度量（不是mem_fragmentation_ratio）。");
		map.put("allocator_frag_bytes","allocator_active和之间的增量allocator_allocated。请参阅关于mem_fragmentation_bytes.");
		map.put("allocator_rss_ratio","allocator_resident和之间的比率allocator_active。这通常表明分配器可以并且可能很快将释放回操作系统的页面。");
		map.put("allocator_rss_bytes","allocator_resident和之间的差值allocator_active");
		map.put("rss_overhead_ratio","used_memory_rss(进程RSS)和之间的比率allocator_resident。这包括与分配器或堆无关的RSS开销。");
		map.put("rss_overhead_bytes","used_memory_rss(进程RSS）和allocator_resident");
		map.put("allocator_allocated","从分配器分配的总字节数，包括内部碎片。通常与used_memory.");
		map.put("allocator_active","分配器活动​​页面中的总字节数，这包括外部碎片。");
		map.put("allocator_resident","分配器中驻留的总字节数(RSS)，这包括可以释放到操作系统的页面（通过MEMORYPURGE，或只是等待）。");
		map.put("mem_not_counted_for_evict","使用的内存不计入键逐出。这基本上是瞬态副本和AOF缓冲区。");
		map.put("mem_clients_slaves","副本客户端使用的内存-从Redis7.0开始，副本缓冲区与复制积压共享内存，因此当副本不触发内存使用量增加时，此字段可以显示0。");
		map.put("mem_clients_normal","普通客户端使用的内存");
		map.put("mem_cluster_links","启用集群模式时，链接到集群总线上的对等点所使用的内存。");
		map.put("mem_aof_buffer","用于AOF和AOF重写缓冲区的瞬态内存");
		map.put("mem_replication_backlog","复制积压使用的内存");
		map.put("mem_total_replication_buffers","复制缓冲区消耗的总内存-在Redis7.0中添加。");
		map.put("mem_allocator","内存分配器，在编译时选择。");
		map.put("active_defrag_running","activedefrag启用时，这表明碎片整理当前是否处于活动状态，以及它打算使用的CPU百分比。");
		map.put("lazyfree_pending_objects","等待释放的对象数（调用UNLINK或使用ASYNC选项FLUSHDB的结果）FLUSHALL");
		map.put("lazyfreed_objects","已被延迟释放的对象数。");
		map.put("loading","指示转储文件的加载是否正在进行的标志");
		map.put("async_loading","当前在提供旧数据时异步加载复制数据集。这意味着repl-diskless-load启用并设置为swapdb。在Redis7.0中添加。");
		map.put("current_cow_peak","子叉子运行时写入时复制内存的峰值大小（以字节为单位）");
		map.put("current_cow_size","子叉子运行时写入时复制内存的大小（以字节为单位）");
		map.put("current_cow_size_age","值的年龄，以秒为单位current_cow_size。");
		map.put("current_fork_perc","当前fork进程的进度百分比。对于AOF和RDB分叉，它是current_save_keys_processedoutof的百分比current_save_keys_total。");
		map.put("current_save_keys_processed","当前保存操作处理的键数");
		map.put("current_save_keys_total","当前保存操作开始时的键数");
		map.put("rdb_changes_since_last_save","自上次转储以来的更改次数");
		map.put("rdb_bgsave_in_progress","指示RDB保存正在进行的标志");
		map.put("rdb_last_save_time","最后一次成功RDB保存的基于Epoch的时间戳");
		map.put("rdb_last_bgsave_status","上次RDB保存操作的状态");
		map.put("rdb_last_bgsave_time_sec","最后一次RDB保存操作的持续时间（以秒为单位）");
		map.put("rdb_current_bgsave_time_sec","正在进行的RDB保存操作的持续时间（如果有）");
		map.put("rdb_last_cow_size","上次RDB保存操作期间的写时复制内存大小（以字节为单位）");
		map.put("rdb_last_load_keys_expired","上次加载RDB期间删除的易失性键的数量。在Redis7.0中添加。");
		map.put("rdb_last_load_keys_loaded","上次加载RDB期间加载的键数。在Redis7.0中添加。");
		map.put("aof_enabled","指示AOF日志记录已激活的标志");
		map.put("aof_rewrite_in_progress","指示AOF重写操作正在进行的标志");
		map.put("aof_rewrite_scheduled","一旦正在进行的RDB保存完成，将安排指示AOF重写操作的标志。");
		map.put("aof_last_rewrite_time_sec","最后一次AOF重写操作的持续时间（以秒为单位）");
		map.put("aof_current_rewrite_time_sec","正在进行的AOF重写操作的持续时间（如果有）");
		map.put("aof_last_bgrewrite_status","上一次AOF重写操作的状态");
		map.put("aof_last_write_status","对AOF的最后一次写操作的状态");
		map.put("aof_last_cow_size","最后一次AOF重写操作期间的写时复制内存的大小（以字节为单位）");
		map.put("module_fork_in_progress","指示模块分叉正在进行的标志");
		map.put("module_fork_last_cow_size","在最后一个模块分叉操作期间，写时复制内存的大小（以字节为单位）");
		map.put("aof_rewrites","自启动以来执行的AOF重写次数");
		map.put("rdb_saves","自启动以来执行的RDB快照数");
		map.put("aof_current_size","AOF当前文件大小");
		map.put("aof_base_size","最近启动或重写时的AOF文件大小");
		map.put("aof_pending_rewrite","一旦正在进行的RDB保存完成，将安排指示AOF重写操作的标志。");
		map.put("aof_buffer_length","AOF缓冲区的大小");
		map.put("aof_rewrite_buffer_length","AOF重写缓冲区的大小。请注意，此字段在Redis7.0中已删除");
		map.put("aof_pending_bio_fsync","后台I/O队列中fsync挂起的作业数");
		map.put("aof_delayed_fsync","延迟fsync计数器");
		map.put("loading_start_time","加载操作开始的基于纪元的时间戳");
		map.put("loading_total_bytes","总文件大小");
		map.put("loading_rdb_used_mem","文件创建时已生成RDB文件的服务器的内存使用情况");
		map.put("loading_loaded_bytes","已加载的字节数");
		map.put("loading_loaded_perc","相同的值以百分比表示");
		map.put("loading_eta_seconds","以秒为单位的ETA加载完成");
		map.put("total_connections_received","服务器接受的连接总数");
		map.put("total_commands_processed","服务器处理的命令总数");
		map.put("instantaneous_ops_per_sec","每秒处理的命令数");
		map.put("total_net_input_bytes","从网络读取的总字节数");
		map.put("total_net_output_bytes","写入网络的总字节数");
		map.put("instantaneous_input_kbps","每秒网络的读取速率，以KB/sec为单位");
		map.put("instantaneous_output_kbps","网络每秒的写入速率，以KB/sec为单位");
		map.put("rejected_connections","由于maxclients限制而被拒绝的连接数");
		map.put("sync_full","与副本完全重新同步的次数");
		map.put("sync_partial_ok","接受的部分重新同步请求的数量");
		map.put("sync_partial_err","被拒绝的部分重新同步请求的数量");
		map.put("expired_keys","密钥过期事件总数");
		map.put("expired_stale_perc","密钥可能过期的百分比");
		map.put("expired_time_cap_reached_count","活动到期周期提前停止的次数");
		map.put("expire_cycle_cpu_milliseconds","在有效到期周期上花费的累计时间量");
		map.put("evicted_keys","由于maxmemory限制而被驱逐的键的数量");
		map.put("evicted_clients","由于maxmemory-clients限制而被驱逐的客户数量。在Redis7.0中添加。");
		map.put("total_eviction_exceeded_time","总时间used_memory大于maxmemory自服务器启动以来，以毫秒为单位");
		map.put("current_eviction_exceeded_time","自used_memory上次升到以上所经过的时间maxmemory，以毫秒为单位");
		map.put("keyspace_hits","在主字典中成功查找键的次数");
		map.put("keyspace_misses","在主字典中查找键失败的次数");
		map.put("pubsub_channels","具有客户端订阅的全球发布/订阅频道数量");
		map.put("pubsub_patterns","具有客户端订阅的全球发布/订阅模式数量");
		map.put("latest_fork_usec","最近一次分叉操作的持续时间，以微秒为单位");
		map.put("total_forks","自服务器启动以来的fork操作总数");
		map.put("migrate_cached_sockets","MIGRATE:为目的打开的套接字数量");
		map.put("slave_expires_tracked_keys","为过期目的而跟踪的密钥数量（仅适用于可写副本）");
		map.put("active_defrag_hits","由活跃的碎片整理过程执行的值重新分配的数量");
		map.put("active_defrag_misses","活动碎片整理进程启动的中止值重新分配的数量");
		map.put("active_defrag_key_hits","积极进行碎片整理的密钥数");
		map.put("active_defrag_key_misses","活动碎片整理过程跳过的键数");
		map.put("total_active_defrag_time","内存碎片超过限制的总时间，以毫秒为单位");
		map.put("current_active_defrag_time","自上次内存碎片超过限制以来经过的时间，以毫秒为单位");
		map.put("tracking_total_keys","服务器正在跟踪的密钥数");
		map.put("tracking_total_items","正在跟踪的项目数，即每个键的客户数之和");
		map.put("tracking_total_prefixes","服务器前缀表中跟踪的前缀数（仅适用于广播模式）");
		map.put("unexpected_error_replies","意外错误回复的数量，即来自AOF加载或复制的错误类型");
		map.put("total_error_replies","发出的错误回复总数，即被拒绝的命令（命令执行前的错误）和失败的命令（命令执行中的错误）的总和");
		map.put("dump_payload_sanitizations","转储有效负载深度完整性验证的总数（请参阅sanitize-dump-payload配置）。");
		map.put("total_reads_processed","处理的读取事件总数");
		map.put("total_writes_processed","处理的写事件总数");
		map.put("io_threaded_reads_processed","主线程和I/O线程处理的读取事件数");
		map.put("io_threaded_writes_processed","主线程和I/O线程处理的写事件数");
		map.put("role","如果实例是无人副本，则值为“master”，如果实例是某个主实例的副本，则值为“slave”。请注意，一个副本可以是另一个副本的主副本（链式复制）。");
		map.put("master_failover_state","正在进行的故障转移的状态（如果有）。");
		map.put("master_replid","Redis服务器的复制ID。");
		map.put("master_replid2","辅助复制ID，用于故障转移后的PSYNC。");
		map.put("master_repl_offset","服务器当前的复制偏移量");
		map.put("second_repl_offset","接受复制ID的偏移量");
		map.put("repl_backlog_active","指示复制积压工作的标志");
		map.put("repl_backlog_size","复制积压缓冲区的总大小（以字节为单位）");
		map.put("repl_backlog_first_byte_offset","复制积压缓冲区的主偏移量");
		map.put("repl_backlog_histlen","复制积压缓冲区中数据的大小（以字节为单位）");
		map.put("master_host","主机的主机或IP地址");
		map.put("master_port","主监听TCP端口");
		map.put("master_link_status","链接状态（上/下）");
		map.put("master_last_io_seconds_ago","自上次与master交互以来的秒数");
		map.put("master_sync_in_progress","表示master正在同步到replica");
		map.put("slave_read_repl_offset","副本实例的读取复制偏移量。");
		map.put("slave_repl_offset","副本实例的复制偏移量");
		map.put("slave_priority","实例作为故障转移候选者的优先级");
		map.put("slave_read_only","指示副本是否为只读的标志");
		map.put("replica_announced","指示副本是否由Sentinel宣布的标志。");
		map.put("master_sync_total_bytes","需要传输的字节总数。这可能是0当大小未知时（例如，当使用repl-diskless-sync配置指令时）");
		map.put("master_sync_read_bytes","已传输的字节数");
		map.put("master_sync_left_bytes","同步完成前剩余的字节数（为0时可能为负master_sync_total_bytes）");
		map.put("master_sync_perc","的百分比master_sync_read_bytes，master_sync_total_bytes或使用loading_rdb_used_memwhen的近似值master_sync_total_bytes为0");
		map.put("master_sync_last_io_seconds_ago","SYNC操作期间自上次传输I/O以来的秒数");
		map.put("master_link_down_since_seconds","自链接断开以来的秒数");
		map.put("connected_slaves","连接的副本数");
		map.put("min_slaves_good_slaves","当前认为好的副本数");
		map.put("slaveXXX","id、IP地址、端口、状态、偏移量、滞后");
		map.put("used_cpu_sys","Redis服务器消耗的系统CPU，是服务器进程所有线程（主线程和后台线程）消耗的系统CPU的总和");
		map.put("used_cpu_user","Redis服务器消耗的用户CPU，是服务器进程所有线程（主线程和后台线程）消耗的用户CPU之和");
		map.put("used_cpu_sys_children","后台进程消耗的系统CPU");
		map.put("used_cpu_user_children","后台进程消耗的用户CPU");
		map.put("used_cpu_sys_main_thread","Redis服务器主线程消耗的系统CPU");
		map.put("used_cpu_user_main_thread","Redis服务器主线程消耗的用户CPU");
		map.put("cluster_enabled","表示启用了Redis集群");

	}

	private String key;
	private String value;
	private String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		this.description = map.get(this.key);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RedisInfo{" + "key='" + key + '\'' + ", value='" + value + '\'' + ", desctiption='" + description + '\'' + '}';
	}
}
