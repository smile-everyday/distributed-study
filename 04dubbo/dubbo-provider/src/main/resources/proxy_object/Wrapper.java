public Object invokeMethod(Object o,String n,Class[]p,Object[]v)throws java.lang.reflect.InvocationTargetException {
    cn.dark.api.IDemoService w;
    try{
        w=((cn.dark.api.IDemoService)$1);
    }catch(Throwable e) {
        throw new IllegalArgumentException(e);
    }

    try{
        if("sayHello".equals($2)&&$3.length==1){
            return($w)w.sayHello((java.lang.String)$4[0]);
        }
    }catch(Throwable e){
        throw new java.lang.reflect.InvocationTargetException(e);
    }
    throw new com.alibaba.dubbo.common.bytecode.NoSuchMethodException("Not found method \""+$2+"\" in class cn.dark.api.IDemoService.");
}