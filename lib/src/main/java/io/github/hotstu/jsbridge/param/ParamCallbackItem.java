/*
 * Copyright (c) 2018 hglf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hotstu.jsbridge.param;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.github.hotstu.jsbridge.annotation.ParamCallback;
import io.github.hotstu.jsbridge.core.NIMJSBridgeException;
import io.github.hotstu.jsbridge.core.NIMJsBridge;
import io.github.hotstu.jsbridge.interact.Interact;
import io.github.hotstu.jsbridge.interact.InteractBuilder;
import io.github.hotstu.jsbridge.interact.Request;
import io.github.hotstu.jsbridge.interact.Response;
import io.github.hotstu.jsbridge.interfaces.IJavaCallback;


/**
 * {@link ParamCallback}注解标注的参数解析
 * <p>
 * Created by huangjun on 2016/10/19.
 */

public final class ParamCallbackItem extends BaseParamItem {


    private final NIMJsBridge bridge;

    public ParamCallbackItem(Class callbackClass, String paramKey, NIMJsBridge bridge) {
        super(callbackClass, paramKey);
        this.bridge = bridge;
    }

    @Override
    public Object convertJson2ParamValue(Interact interact) {
        if (interact == null || interact.getCallbackId() == null) {
            return null;
        }
        final String resId = interact.getCallbackId();

        // 返回IJavaReplyToJS或其子类的动态代理对象，调用IJavaReplyToJs接口中的方法，会转向执行下面invoke
        return Proxy.newProxyInstance(paramType.getClassLoader(), new Class<?>[]{paramType},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // method是具体IJavaReplyToJs的具体方法,一般是replyToJs(...)
                        Response response = InteractBuilder.createResponse(null);
                        response.setCallbackId(resId);
                        Params params = Params.createParams(method, bridge);
                        params.convertParamValues2Json(response, args);
                        //TODO bug，静态引用导致多webview不能取得正确的回调
                        if (bridge != null) {
                            // js->java->replyToJs->js
                            bridge.sendData2JS(response);
                        } else {
                            throw new NIMJSBridgeException(NIMJsBridge.class.getName() + " should be inited");
                        }
                        return new Object();
                    }
                }
        );
    }

    @Override
    public void convertParamValue2Json(Interact interact, Object obj) {
        // java->js,request记录javaCallback
        if (interact == null || !Interact.isRequest(interact)
                || obj == null || !(obj instanceof IJavaCallback)) {
            return;
        }

        ((Request) interact).setJavaCallback((IJavaCallback) obj);
    }
}
