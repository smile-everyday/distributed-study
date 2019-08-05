package cn.dark.rpc.server;

import cn.dark.rpc.server.zk.ServiceRepository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 服务端处理消息类
 *
 * @author dark
 * @date 2019-07-16
 */
public class ProcessHandler implements Runnable {

    private Socket socket;

    public ProcessHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            // 反序列化客户端传过来的对象，并通过反射调用服务端对象的方法
            RpcEntity entity = (RpcEntity) ois.readObject();
            Object result = invokeService(entity);

            // 将结果返回给客户端
            oos.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object invokeService(RpcEntity entity) throws Exception {
        // 通过参数类型和参数名称拿到Method，再反射调用方法
        Object[] args = entity.getArgs();
        Class[] params = new Class[args.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = args[i].getClass();
        }

        // 根据客户端传入的服务名调用相应的服务对象的方法
        String serviceName = entity.getClassName();
        Object service = ServiceRepository.getService(serviceName);
        Method method = service.getClass().getMethod(entity.getMethodName(), params);
        return method.invoke(service, args);
    }

}
