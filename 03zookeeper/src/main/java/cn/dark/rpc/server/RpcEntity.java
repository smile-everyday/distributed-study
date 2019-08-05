package cn.dark.rpc.server;

import java.io.Serializable;

/**
 * 传输对象
 *
 * @author dark
 * @date 2019-07-16
 */
public class RpcEntity implements Serializable {

    private static final long serialVersionUID = -8789719821222021770L;

    private String className; // 全类名
    private String methodName; // 调用方法名
    private Object[] args; // 方法的参数值

    public RpcEntity(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
