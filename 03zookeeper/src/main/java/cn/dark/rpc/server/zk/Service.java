package cn.dark.rpc.server.zk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在服务对象的类上
 *
 * @author dark
 * @date 2019-07-18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * 对外发布的服务名称
     */
    Class<?> value();

}
