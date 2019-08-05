package cn.dark.rpc;

/**
 * @author dark
 * @date 2019-07-18
 */
public interface ZkConfig {

    // zk地址，集群使用，分隔，需要指明端口号
    String CONNECT_STR = "192.168.0.106:2181,192.168.0.108:2181,192.168.0.109:2181";
    // 服务注册根路径
    String NAMESPACE = "registry";

}
