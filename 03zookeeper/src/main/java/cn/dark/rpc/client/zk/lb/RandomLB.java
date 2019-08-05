package cn.dark.rpc.client.zk.lb;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡器
 *
 * @author dark
 * @date 2019-07-19
 */
public class RandomLB extends AbstractLB {

    @Override
    protected String doSelect(List<String> hosts) {
        Random random = new Random();
        return hosts.get(random.nextInt(hosts.size()));
    }

}
