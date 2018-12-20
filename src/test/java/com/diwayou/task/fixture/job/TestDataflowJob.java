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

package com.diwayou.task.fixture.job;

import com.diwayou.task.api.ShardingContext;
import com.diwayou.task.api.dataflow.DataflowJob;

import java.util.List;

public final class TestDataflowJob implements DataflowJob<Object> {

    private final JobCaller jobCaller;

    public TestDataflowJob(JobCaller jobCaller) {
        this.jobCaller = jobCaller;
    }

    @Override
    public List<Object> fetchData(final ShardingContext shardingContext) {
        return jobCaller.fetchData(shardingContext.getShardingItem());
    }

    @Override
    public void processData(final ShardingContext shardingContext, final List<Object> data) {
        for (Object each : data) {
            jobCaller.processData(each);
        }
    }
}
