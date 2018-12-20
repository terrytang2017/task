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

package com.diwayou.task.fixture.config;

import com.diwayou.task.config.JobCoreConfiguration;
import com.diwayou.task.config.JobRootConfiguration;
import com.diwayou.task.config.JobTypeConfiguration;
import com.diwayou.task.config.dataflow.DataflowJobConfiguration;
import com.diwayou.task.executor.handler.JobProperties;
import com.diwayou.task.fixture.ShardingContextsBuilder;
import com.diwayou.task.fixture.handler.IgnoreJobExceptionHandler;
import com.diwayou.task.fixture.job.TestDataflowJob;

public final class TestDataflowJobConfiguration implements JobRootConfiguration {

    private final boolean streamingProcess;

    public TestDataflowJobConfiguration(boolean streamingProcess) {
        this.streamingProcess = streamingProcess;
    }

    @Override
    public JobTypeConfiguration getTypeConfig() {
        return new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(ShardingContextsBuilder.JOB_NAME, "0/1 * * * * ?", 3)
                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), IgnoreJobExceptionHandler.class.getCanonicalName()).build(),
                TestDataflowJob.class.getCanonicalName(), streamingProcess);
    }
}
