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

package com.diwayou.task.internal.failover;

import com.diwayou.task.api.strategy.JobInstance;
import com.diwayou.task.config.JobCoreConfiguration;
import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.config.simple.SimpleJobConfiguration;
import com.diwayou.task.fixture.LiteJsonConstants;
import com.diwayou.task.fixture.job.TestSimpleJob;
import com.diwayou.task.internal.config.ConfigurationService;
import com.diwayou.task.internal.listener.AbstractJobListener;
import com.diwayou.task.internal.schedule.JobRegistry;
import com.diwayou.task.internal.sharding.ShardingService;
import com.diwayou.task.internal.storage.JobNodeStorage;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.util.ReflectionUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public final class FailoverListenerManagerTest {

    private final FailoverListenerManager failoverListenerManager = new FailoverListenerManager(null, "test_job");
    @Mock
    private JobNodeStorage jobNodeStorage;
    @Mock
    private ConfigurationService configService;
    @Mock
    private ShardingService shardingService;
    @Mock
    private FailoverService failoverService;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setFieldValue(failoverListenerManager, failoverListenerManager.getClass().getSuperclass().getDeclaredField("jobNodeStorage"), jobNodeStorage);
        ReflectionUtils.setFieldValue(failoverListenerManager, "configService", configService);
        ReflectionUtils.setFieldValue(failoverListenerManager, "shardingService", shardingService);
        ReflectionUtils.setFieldValue(failoverListenerManager, "failoverService", failoverService);
    }

    @Test
    public void assertStart() {
        failoverListenerManager.start();
        verify(jobNodeStorage, times(2)).addDataListener(ArgumentMatchers.<AbstractJobListener>any());
    }

    @Test
    public void assertJobCrashedJobListenerWhenFailoverDisabled() {
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(failoverService, times(0)).failoverIfNecessary();
    }

    @Test
    public void assertJobCrashedJobListenerWhenIsNotNodeRemoved() {
        when(configService.load(true)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).failover(true).build(), TestSimpleJob.class.getCanonicalName())).build());
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_ADDED, "");
        verify(failoverService, times(0)).failoverIfNecessary();
    }

    @Test
    public void assertJobCrashedJobListenerWhenIsNotInstancesPath() {
        when(configService.load(true)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).failover(true).build(), TestSimpleJob.class.getCanonicalName())).build());
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/other/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(failoverService, times(0)).failoverIfNecessary();
    }

    @Test
    public void assertJobCrashedJobListenerWhenIsSameInstance() {
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
        when(configService.load(true)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).failover(true).build(), TestSimpleJob.class.getCanonicalName())).build());
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(failoverService, times(0)).failoverIfNecessary();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertJobCrashedJobListenerWhenIsOtherInstanceCrashed() {
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
        when(configService.load(true)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).failover(true).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(shardingService.getShardingItems("127.0.0.1@-@1")).thenReturn(Arrays.asList(0, 2));
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/instances/127.0.0.1@-@1", Type.NODE_REMOVED, "");
        verify(failoverService).setCrashedFailoverFlag(0);
        verify(failoverService).setCrashedFailoverFlag(2);
        verify(failoverService, times(2)).failoverIfNecessary();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertJobCrashedJobListenerWhenIsOtherFailoverInstanceCrashed() {
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
        when(configService.load(true)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).failover(true).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(failoverService.getFailoverItems("127.0.0.1@-@1")).thenReturn(Collections.singletonList(1));
        when(shardingService.getShardingItems("127.0.0.1@-@1")).thenReturn(Arrays.asList(0, 2));
        failoverListenerManager.new JobCrashedJobListener().dataChanged("/test_job/instances/127.0.0.1@-@1", Type.NODE_REMOVED, "");
        verify(failoverService).setCrashedFailoverFlag(1);
        verify(failoverService).failoverIfNecessary();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertFailoverSettingsChangedJobListenerWhenIsNotFailoverPath() {
        failoverListenerManager.new FailoverSettingsChangedJobListener().dataChanged("/test_job/other", Type.NODE_ADDED, LiteJsonConstants.getJobJson());
        verify(failoverService, times(0)).removeFailoverInfo();
    }

    @Test
    public void assertFailoverSettingsChangedJobListenerWhenIsFailoverPathButNotUpdate() {
        failoverListenerManager.new FailoverSettingsChangedJobListener().dataChanged("/test_job/config", Type.NODE_ADDED, "");
        verify(failoverService, times(0)).removeFailoverInfo();
    }

    @Test
    public void assertFailoverSettingsChangedJobListenerWhenIsFailoverPathAndUpdateButEnableFailover() {
        failoverListenerManager.new FailoverSettingsChangedJobListener().dataChanged("/test_job/config", Type.NODE_UPDATED, LiteJsonConstants.getJobJson());
        verify(failoverService, times(0)).removeFailoverInfo();
    }

    @Test
    public void assertFailoverSettingsChangedJobListenerWhenIsFailoverPathAndUpdateButDisableFailover() {
        failoverListenerManager.new FailoverSettingsChangedJobListener().dataChanged("/test_job/config", Type.NODE_UPDATED, LiteJsonConstants.getJobJsonWithFailover(false));
        verify(failoverService).removeFailoverInfo();
    }
}
