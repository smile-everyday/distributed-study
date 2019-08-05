package cn.dark.rpc.server.zk;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 服务仓库
 *
 * @author dark
 * @date 2019-07-18
 */
public class ServiceRepository {

    /**
     * 存储服务名称和服务对象的关系
     */
    private static final Map<String, Object> SERVICE_CACHE = new HashMap<>();

    /**
     * 绑定服务名称和服务对象
     *
     * @param service 服务对象
     * @date 2019-07-18
     *
     */
    public static void bind(Object service) {
        String serviceName = service.getClass().getAnnotation(Service.class).value().getName();
        SERVICE_CACHE.put(serviceName, service);
    }

    /**
     * 获取已发布的服务名称
     *
     * @return java.util.Set<java.lang.String>
     * @date 2019-07-18
     *
     */
    public static Set<String> getServiceNames() {
        return SERVICE_CACHE.keySet();
    }

    /**
     * 根据服务名称获取到服务对象
     *
     * @param serviceName
     * @return java.lang.Object
     * @date 2019-07-19
     *
     */
    public static Object getService(String serviceName) {
        return SERVICE_CACHE.get(serviceName);
    }
}
