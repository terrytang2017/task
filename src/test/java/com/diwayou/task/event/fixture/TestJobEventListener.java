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

package com.diwayou.task.event.fixture;

import com.diwayou.task.event.JobEventListener;
import com.diwayou.task.event.type.JobExecutionEvent;
import com.diwayou.task.event.type.JobStatusTraceEvent;

public final class TestJobEventListener extends TestJobEventIdentity implements JobEventListener {

    private static volatile boolean executionEventCalled;

    private final JobEventCaller jobEventCaller;

    public TestJobEventListener(JobEventCaller jobEventCaller) {
        this.jobEventCaller = jobEventCaller;
    }

    public static void reset() {
        executionEventCalled = false;
    }

    public static boolean isExecutionEventCalled() {
        return executionEventCalled;
    }

    @Override
    public void listen(final JobExecutionEvent jobExecutionEvent) {
        jobEventCaller.call();
        executionEventCalled = true;
    }

    @Override
    public void listen(final JobStatusTraceEvent jobStatusTraceEvent) {
        jobEventCaller.call();
    }
}