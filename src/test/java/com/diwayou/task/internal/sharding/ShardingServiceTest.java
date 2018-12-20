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

package com.diwayou.task.internal.sharding;

import com.diwayou.task.api.strategy.JobInstance;
import com.diwayou.task.config.JobCoreConfiguration;
import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.config.simple.SimpleJobConfiguration;
import com.diwayou.task.fixture.job.TestSimpleJob;
import com.diwayou.task.internal.config.ConfigurationService;
import com.diwayou.task.internal.election.LeaderService;
import com.diwayou.task.internal.instance.InstanceNode;
import com.diwayou.task.internal.instance.InstanceService;
import com.diwayou.task.internal.schedule.JobRegistry;
import com.diwayou.task.internal.schedule.JobScheduleController;
import com.diwayou.task.internal.server.ServerService;
import com.diwayou.task.internal.storage.JobNodeStorage;
import com.diwayou.task.internal.storage.TransactionExecutionCallback;
import com.diwayou.task.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.api.transaction.CuratorTransactionBridge;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.TransactionCreateBuilder;
import org.apache.curator.framework.api.transaction.TransactionDeleteBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.util.ReflectionUtils;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public final class ShardingServiceTest {

    private final ShardingService shardingService = new ShardingService(null, "test_job");
    @Mock
    private CoordinatorRegistryCenter regCenter;
    @Mock
    private JobScheduleController jobScheduleController;
    @Mock
    private JobNodeStorage jobNodeStorage;
    @Mock
    private LeaderService leaderService;
    @Mock
    private ConfigurationService configService;
    @Mock
    private ExecutionService executionService;
    @Mock
    private ServerService serverService;
    @Mock
    private InstanceService instanceService;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setFieldValue(shardingService, "jobNodeStorage", jobNodeStorage);
        ReflectionUtils.setFieldValue(shardingService, "leaderService", leaderService);
        ReflectionUtils.setFieldValue(shardingService, "configService", configService);
        ReflectionUtils.setFieldValue(shardingService, "executionService", executionService);
        ReflectionUtils.setFieldValue(shardingService, "instanceService", instanceService);
        ReflectionUtils.setFieldValue(shardingService, "serverService", serverService);
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
    }

    @Test
    public void assertSetReshardingFlag() {
        shardingService.setReshardingFlag();
        verify(jobNodeStorage).createJobNodeIfNeeded("leader/sharding/necessary");
    }

    @Test
    public void assertIsNeedSharding() {
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/necessary")).thenReturn(true);
        assertTrue(shardingService.isNeedSharding());
    }

    @Test
    public void assertShardingWhenUnnecessary() {
        shardingService.shardingIfNecessary();
        verify(jobNodeStorage, times(0)).fillEphemeralJobNode(ShardingNode.PROCESSING, "");
    }

    @Test
    public void assertShardingWithoutAvailableJobInstances() {
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/necessary")).thenReturn(true);
        shardingService.shardingIfNecessary();
        verify(jobNodeStorage, times(0)).fillEphemeralJobNode(ShardingNode.PROCESSING, "");
    }

    @Test
    public void assertShardingWhenIsNotLeader() {
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/necessary")).thenReturn(true, false);
        when(instanceService.getAvailableJobInstances()).thenReturn(Collections.singletonList(new JobInstance("127.0.0.1@-@0")));
        when(leaderService.isLeaderUntilBlock()).thenReturn(false);
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/processing")).thenReturn(true, false);
        shardingService.shardingIfNecessary();
        verify(jobNodeStorage, times(0)).fillEphemeralJobNode(ShardingNode.PROCESSING, "");
    }

    @Test
    public void assertShardingNecessaryWhenMonitorExecutionEnabledAndIncreaseShardingTotalCount() {
        when(instanceService.getAvailableJobInstances()).thenReturn(Collections.singletonList(new JobInstance("127.0.0.1@-@0")));
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/necessary")).thenReturn(true);
        when(leaderService.isLeaderUntilBlock()).thenReturn(true);
        when(configService.load(false)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).monitorExecution(true).build());
        when(executionService.hasRunningItems()).thenReturn(true, false);
        when(jobNodeStorage.getJobNodeChildrenKeys(ShardingNode.ROOT)).thenReturn(Arrays.asList("0", "1"));
        shardingService.shardingIfNecessary();
        verify(executionService, times(2)).hasRunningItems();
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/0/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/0");
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/1/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/1");
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/2/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/2");
        verify(jobNodeStorage).fillEphemeralJobNode("leader/sharding/processing", "");
        verify(jobNodeStorage).executeInTransaction(ArgumentMatchers.any(TransactionExecutionCallback.class));
    }

    @Test
    public void assertShardingNecessaryWhenMonitorExecutionDisabledAndDecreaseShardingTotalCount() {
        when(instanceService.getAvailableJobInstances()).thenReturn(Collections.singletonList(new JobInstance("127.0.0.1@-@0")));
        when(jobNodeStorage.isJobNodeExisted("leader/sharding/necessary")).thenReturn(true);
        when(leaderService.isLeaderUntilBlock()).thenReturn(true);
        when(configService.load(false)).thenReturn(LiteJobConfiguration.newBuilder(
                new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).monitorExecution(false).build());
        when(jobNodeStorage.getJobNodeChildrenKeys(ShardingNode.ROOT)).thenReturn(Arrays.asList("0", "1", "2", "3"));
        shardingService.shardingIfNecessary();
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/0/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/0");
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/1/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/1");
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/2/instance");
        verify(jobNodeStorage).createJobNodeIfNeeded("sharding/2");
        verify(jobNodeStorage, times(0)).removeJobNodeIfExisted("execution/2");
        verify(jobNodeStorage).removeJobNodeIfExisted("sharding/3");
        verify(jobNodeStorage).fillEphemeralJobNode("leader/sharding/processing", "");
        verify(jobNodeStorage).executeInTransaction(ArgumentMatchers.any(TransactionExecutionCallback.class));
    }

    @Test
    public void assertGetShardingItemsWithNotAvailableServer() {
        assertThat(shardingService.getShardingItems("127.0.0.1@-@0"), is(Collections.<Integer>emptyList()));
    }

    @Test
    public void assertGetShardingItemsWithEnabledServer() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        when(serverService.isAvailableServer("127.0.0.1")).thenReturn(true);
        when(configService.load(true)).thenReturn(
                LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(jobNodeStorage.getJobNodeData("sharding/0/instance")).thenReturn("127.0.0.1@-@0");
        when(jobNodeStorage.getJobNodeData("sharding/1/instance")).thenReturn("127.0.0.1@-@1");
        when(jobNodeStorage.getJobNodeData("sharding/2/instance")).thenReturn("127.0.0.1@-@0");
        assertThat(shardingService.getShardingItems("127.0.0.1@-@0"), is(Arrays.asList(0, 2)));
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertGetLocalShardingItemsWithInstanceShutdown() {
        assertThat(shardingService.getLocalShardingItems(), is(Collections.<Integer>emptyList()));
    }

    @Test
    public void assertGetLocalShardingItemsWithDisabledServer() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        assertThat(shardingService.getLocalShardingItems(), is(Collections.<Integer>emptyList()));
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertGetLocalShardingItemsWithEnabledServer() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        when(serverService.isAvailableServer("127.0.0.1")).thenReturn(true);
        when(configService.load(true)).thenReturn(
                LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(jobNodeStorage.getJobNodeData("sharding/0/instance")).thenReturn("127.0.0.1@-@0");
        when(jobNodeStorage.getJobNodeData("sharding/1/instance")).thenReturn("127.0.0.1@-@1");
        when(jobNodeStorage.getJobNodeData("sharding/2/instance")).thenReturn("127.0.0.1@-@0");
        assertThat(shardingService.getLocalShardingItems(), is(Arrays.asList(0, 2)));
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertHasShardingInfoInOfflineServers() {
        when(jobNodeStorage.getJobNodeChildrenKeys(InstanceNode.ROOT)).thenReturn(Arrays.asList("host0@-@0", "host0@-@1"));
        when(configService.load(true)).thenReturn(
                LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(0))).thenReturn("host0@-@0");
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(1))).thenReturn("host0@-@1");
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(2))).thenReturn("host0@-@2");
        assertTrue(shardingService.hasShardingInfoInOfflineServers());
    }

    @Test
    public void assertHasNotShardingInfoInOfflineServers() {
        when(jobNodeStorage.getJobNodeChildrenKeys(InstanceNode.ROOT)).thenReturn(Arrays.asList("host0@-@0", "host0@-@1"));
        when(configService.load(true)).thenReturn(
                LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(0))).thenReturn("host0@-@0");
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(1))).thenReturn("host0@-@1");
        when(jobNodeStorage.getJobNodeData(ShardingNode.getInstanceNode(2))).thenReturn("host0@-@0");
        assertFalse(shardingService.hasShardingInfoInOfflineServers());
    }

    @Test
    public void assertPersistShardingInfoTransactionExecutionCallback() throws Exception {
        CuratorTransactionFinal curatorTransactionFinal = mock(CuratorTransactionFinal.class);
        TransactionCreateBuilder transactionCreateBuilder = mock(TransactionCreateBuilder.class);
        TransactionDeleteBuilder transactionDeleteBuilder = mock(TransactionDeleteBuilder.class);
        CuratorTransactionBridge curatorTransactionBridge = mock(CuratorTransactionBridge.class);
        when(curatorTransactionFinal.create()).thenReturn(transactionCreateBuilder);
        when(configService.load(true)).thenReturn(
                LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder("test_job", "0/1 * * * * ?", 3).build(), TestSimpleJob.class.getCanonicalName())).build());
        when(transactionCreateBuilder.forPath("/test_job/sharding/0/instance", "host0@-@0".getBytes())).thenReturn(curatorTransactionBridge);
        when(transactionCreateBuilder.forPath("/test_job/sharding/1/instance", "host0@-@0".getBytes())).thenReturn(curatorTransactionBridge);
        when(transactionCreateBuilder.forPath("/test_job/sharding/2/instance", "host0@-@0".getBytes())).thenReturn(curatorTransactionBridge);
        when(curatorTransactionBridge.and()).thenReturn(curatorTransactionFinal);
        when(curatorTransactionFinal.delete()).thenReturn(transactionDeleteBuilder);
        when(transactionDeleteBuilder.forPath("/test_job/leader/sharding/necessary")).thenReturn(curatorTransactionBridge);
        when(curatorTransactionBridge.and()).thenReturn(curatorTransactionFinal);
        when(curatorTransactionFinal.delete()).thenReturn(transactionDeleteBuilder);
        when(transactionDeleteBuilder.forPath("/test_job/leader/sharding/processing")).thenReturn(curatorTransactionBridge);
        when(curatorTransactionBridge.and()).thenReturn(curatorTransactionFinal);
        Map<JobInstance, List<Integer>> shardingResult = new HashMap<>();
        shardingResult.put(new JobInstance("host0@-@0"), Arrays.asList(0, 1, 2));
        ShardingService.PersistShardingInfoTransactionExecutionCallback actual = shardingService.new PersistShardingInfoTransactionExecutionCallback(shardingResult);
        actual.execute(curatorTransactionFinal);
        verify(curatorTransactionFinal, times(3)).create();
        verify(curatorTransactionFinal, times(2)).delete();
        verify(transactionDeleteBuilder).forPath("/test_job/leader/sharding/necessary");
        verify(transactionDeleteBuilder).forPath("/test_job/leader/sharding/processing");
        verify(curatorTransactionBridge, times(5)).and();
    }
}
