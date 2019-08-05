package cn.dark.rpc.client.zk.lb;

import java.util.List;

/**
 * 负载均衡接口
 *
 * @author dark
 * @date 2019-07-19
 */
public interface ILB {

    String selectHost(List<String> hosts);

}
