package cn.dark.rpc.client.zk.lb;

import java.util.List;

/**
 * @author dark
 * @date 2019-07-19
 */
public abstract class AbstractLB implements ILB {

    /**
     * 负载均衡，采用模板模式的思想提高扩展性，该方法只是抽离出公共的代码，
     * 具体的算法由doSelect方法实现
     *
     * @param hosts
     * @return java.lang.String
     * @date 2019-07-19
     *
     */
    @Override
    public String selectHost(List<String> hosts) {
        if (hosts == null || hosts.size() == 0) {
            return null;
        }

        if (hosts.size() == 1) {
            return hosts.get(0);
        }

        // 节点中都包含分隔符，统一处理后返回
        String node = doSelect(hosts);
        return node.replace("/", "");
    }

    /**
     * 抽象的负载算法，根据需求扩展
     *
     * @param hosts
     * @return java.lang.String
     * @date 2019-07-19
     *
     */
    protected abstract String doSelect(List<String> hosts);

}
