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

package io.github.hotstu.jsbridge.interfaces;


import io.github.hotstu.jsbridge.annotation.Param;
import io.github.hotstu.jsbridge.annotation.ParamResponseStatus;
import io.github.hotstu.jsbridge.interact.Response;

/**
 * java reply content string to js
 * <p>
 * Created by huangjun on 2016/10/20.
 */

public interface IJavaReplyToJsString {

    String RESPONSE_CONTENT_INFO = "content";

    void replyToJs(@ParamResponseStatus(Response.RESPONSE_DATA_STATUS) int status,
                   @ParamResponseStatus(Response.RESPONSE_DATA_MSG) String msg,
                   @Param(RESPONSE_CONTENT_INFO) String content);
}
