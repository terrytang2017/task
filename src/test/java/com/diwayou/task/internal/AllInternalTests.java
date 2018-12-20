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

package com.diwayou.task.internal;

import com.diwayou.task.internal.config.ConfigurationNodeTest;
import com.diwayou.task.internal.config.ConfigurationServiceTest;
import com.diwayou.task.internal.config.LiteJobConfigurationGsonFactoryTest;
import com.diwayou.task.internal.config.RescheduleListenerManagerTest;
import com.diwayou.task.internal.election.ElectionListenerManagerTest;
import com.diwayou.task.internal.election.LeaderNodeTest;
import com.diwayou.task.internal.election.LeaderServiceTest;
import com.diwayou.task.internal.failover.FailoverListenerManagerTest;
import com.diwayou.task.internal.failover.FailoverNodeTest;
import com.diwayou.task.internal.failover.FailoverServiceTest;
import com.diwayou.task.internal.guarantee.GuaranteeNodeTest;
import com.diwayou.task.internal.guarantee.GuaranteeServiceTest;
import com.diwayou.task.internal.instance.InstanceNodeTest;
import com.diwayou.task.internal.instance.InstanceServiceTest;
import com.diwayou.task.internal.instance.ShutdownListenerManagerTest;
import com.diwayou.task.internal.instance.TriggerListenerManagerTest;
import com.diwayou.task.internal.listener.JobListenerTest;
import com.diwayou.task.internal.listener.ListenerManagerTest;
import com.diwayou.task.internal.listener.RegistryCenterConnectionStateListenerTest;
import com.diwayou.task.internal.monitor.MonitorServiceDisableTest;
import com.diwayou.task.internal.monitor.MonitorServiceEnableTest;
import com.diwayou.task.internal.reconcile.ReconcileServiceTest;
import com.diwayou.task.internal.schedule.*;
import com.diwayou.task.internal.server.ServerNodeTest;
import com.diwayou.task.internal.server.ServerServiceTest;
import com.diwayou.task.internal.sharding.*;
import com.diwayou.task.internal.storage.JobNodePathTest;
import com.diwayou.task.internal.storage.JobNodeStorageTest;
import com.diwayou.task.internal.util.SensitiveInfoUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        JobNodePathTest.class,
        JobNodeStorageTest.class,
        SensitiveInfoUtilsTest.class,
        ConfigurationServiceTest.class,
        ConfigurationNodeTest.class,
        RescheduleListenerManagerTest.class,
        LiteJobConfigurationGsonFactoryTest.class,
        LeaderServiceTest.class,
        LeaderNodeTest.class,
        ElectionListenerManagerTest.class,
        ServerServiceTest.class,
        InstanceNodeTest.class,
        InstanceServiceTest.class,
        ShutdownListenerManagerTest.class,
        TriggerListenerManagerTest.class,
        ShardingServiceTest.class,
        ServerNodeTest.class,
        ShardingListenerManagerTest.class,
        ExecutionContextServiceTest.class,
        ExecutionServiceTest.class,
        MonitorExecutionListenerManagerTest.class,
        ShardingNodeTest.class,
        FailoverServiceTest.class,
        FailoverNodeTest.class,
        FailoverListenerManagerTest.class,
        JobRegistryTest.class,
        JobScheduleControllerTest.class,
        JobTriggerListenerTest.class,
        ListenerManagerTest.class,
        JobListenerTest.class,
        MonitorServiceEnableTest.class,
        MonitorServiceDisableTest.class,
        GuaranteeNodeTest.class,
        GuaranteeServiceTest.class,
        SchedulerFacadeTest.class,
        LiteJobFacadeTest.class,
        ReconcileServiceTest.class,
        RegistryCenterConnectionStateListenerTest.class
})
public final class AllInternalTests {
}
