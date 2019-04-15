/*
 * Copyright 2018 klw(213539@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.keran213539.commonOkHttp.callback;

import okhttp3.Response;

/**
 * @ClassName: IAsyncCallback
 * @Description: 异步http请求回调接口,支持 Lambda 表达式
 * @author klw
 * @date 2018年4月19日 09:45:59
 */
@FunctionalInterface
public interface IAsyncCallback4Response {

    /**
     * @Title: doCallback
     * @Description: 异步回调接口的执行方法,传入 okhttp3.Response
     * @param responseBody
     */
    public void doCallback(Response response);
    
}
