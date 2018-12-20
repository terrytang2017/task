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

package com.diwayou.task.integrate.std.dataflow;

import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.fixture.util.JobConfigurationUtil;
import com.diwayou.task.integrate.AbstractBaseStdJobAutoInitTest;
import com.diwayou.task.integrate.WaitingUtils;
import com.diwayou.task.integrate.fixture.dataflow.OneOffDataflowElasticJob;
import com.diwayou.task.integrate.fixture.dataflow.StreamingDataflowElasticJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public final class StreamingDataflowElasticJobForNotMonitorTest extends AbstractBaseStdJobAutoInitTest {

    public StreamingDataflowElasticJobForNotMonitorTest() {
        super(StreamingDataflowElasticJob.class);
    }

    @Before
    @After
    public void reset() {
        StreamingDataflowElasticJob.reset();
    }

    @Override
    protected void setLiteJobConfig(final LiteJobConfiguration liteJobConfig) {
        JobConfigurationUtil.setFieldValue(liteJobConfig, "monitorExecution", false);
        JobConfigurationUtil.setFieldValue(liteJobConfig.getTypeConfig(), "streamingProcess", true);
    }

    @Test
    public void assertJobInit() {
        WaitingUtils.waitingUntilTrueOrTimeout((Void) -> StreamingDataflowElasticJob.isCompleted(), 3 * 1000);
        assertTrue(getRegCenter().isExisted("/" + getJobName() + "/sharding"));
    }
}
