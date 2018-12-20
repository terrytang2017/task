/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package com.diwayou.task.executor.handler.impl;

import com.diwayou.task.executor.handler.JobExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认作业异常处理器.
 *
 * @author zhangliang
 */
public final class DefaultJobExceptionHandler implements JobExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultJobExceptionHandler.class);

    @Override
    public void handleException(final String jobName, final Throwable cause) {
        log.error(String.format("Job '%s' exception occur in job processing", jobName), cause);
    }
}
