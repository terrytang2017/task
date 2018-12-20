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

package com.diwayou.task.fixture.util;

import com.diwayou.task.api.dataflow.DataflowJob;
import com.diwayou.task.config.JobCoreConfiguration;
import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.config.dataflow.DataflowJobConfiguration;
import com.diwayou.task.config.simple.SimpleJobConfiguration;
import com.diwayou.task.fixture.job.TestSimpleJob;
import org.unitils.util.ReflectionUtils;

public final class JobConfigurationUtil {

    public static void setFieldValue(final Object config, final String fieldName, final Object fieldValue) {
        try {
            ReflectionUtils.setFieldValue(config, config.getClass().getDeclaredField(fieldName), fieldValue);
        } catch (final NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static LiteJobConfiguration createSimpleLiteJobConfiguration() {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build();
    }

    public static LiteJobConfiguration createSimpleLiteJobConfiguration(final boolean overwrite) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(),
                TestSimpleJob.class.getCanonicalName())).overwrite(overwrite).build();
    }

    public static LiteJobConfiguration createDataflowLiteJobConfiguration() {
        return LiteJobConfiguration.newBuilder(
                new DataflowJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), DataflowJob.class.getCanonicalName(), false)).build();
    }
}
