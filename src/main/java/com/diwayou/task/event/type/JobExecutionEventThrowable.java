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

package com.diwayou.task.event.type;

/**
 * 作业执行事件Throwable.
 *
 * @author liguangyun
 */
public final class JobExecutionEventThrowable {

    private final Throwable throwable;

    private String plainText;

    public JobExecutionEventThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public JobExecutionEventThrowable(Throwable throwable, String plainText) {
        this.throwable = throwable;
        this.plainText = plainText;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getPlainText() {
        return plainText;
    }

    @Override
    public String toString() {
        return "JobExecutionEventThrowable{" +
                "plainText='" + plainText + '\'' +
                '}';
    }
}
