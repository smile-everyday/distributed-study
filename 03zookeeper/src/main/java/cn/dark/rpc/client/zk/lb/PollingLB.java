package cn.dark.rpc.client.zk.lb;

import java.util.List;

/**
 * 轮询负载均衡器
 *
 * @author dark
 * @date 2019-07-20
 */
public class PollingLB extends AbstractLB {

    private static int count = 0; // 轮询计数器

    @Override
    protected String doSelect(List<String> hosts) {
        if (count >= hosts.size()) {
            count = 0;
        }
        return hosts.get(count++);
    }

}
