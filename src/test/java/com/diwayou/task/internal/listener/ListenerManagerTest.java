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

package com.diwayou.task.internal.listener;

import com.diwayou.task.api.listener.ElasticJobListener;
import com.diwayou.task.internal.config.RescheduleListenerManager;
import com.diwayou.task.internal.election.ElectionListenerManager;
import com.diwayou.task.internal.failover.FailoverListenerManager;
import com.diwayou.task.internal.guarantee.GuaranteeListenerManager;
import com.diwayou.task.internal.instance.ShutdownListenerManager;
import com.diwayou.task.internal.instance.TriggerListenerManager;
import com.diwayou.task.internal.sharding.MonitorExecutionListenerManager;
import com.diwayou.task.internal.sharding.ShardingListenerManager;
import com.diwayou.task.internal.storage.JobNodeStorage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.util.ReflectionUtils;

import java.util.Collections;

import static org.mockito.Mockito.verify;

public class ListenerManagerTest {

    private final ListenerManager listenerManager = new ListenerManager(null, "test_job", Collections.<ElasticJobListener>emptyList());
    @Mock
    private JobNodeStorage jobNodeStorage;
    @Mock
    private ElectionListenerManager electionListenerManager;
    @Mock
    private ShardingListenerManager shardingListenerManager;
    @Mock
    private FailoverListenerManager failoverListenerManager;
    @Mock
    private MonitorExecutionListenerManager monitorExecutionListenerManager;
    @Mock
    private ShutdownListenerManager shutdownListenerManager;
    @Mock
    private TriggerListenerManager triggerListenerManager;
    @Mock
    private RescheduleListenerManager rescheduleListenerManager;
    @Mock
    private GuaranteeListenerManager guaranteeListenerManager;
    @Mock
    private RegistryCenterConnectionStateListener regCenterConnectionStateListener;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setFieldValue(listenerManager, "jobNodeStorage", jobNodeStorage);
        ReflectionUtils.setFieldValue(listenerManager, "electionListenerManager", electionListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "shardingListenerManager", shardingListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "failoverListenerManager", failoverListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "monitorExecutionListenerManager", monitorExecutionListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "shutdownListenerManager", shutdownListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "triggerListenerManager", triggerListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "rescheduleListenerManager", rescheduleListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "guaranteeListenerManager", guaranteeListenerManager);
        ReflectionUtils.setFieldValue(listenerManager, "regCenterConnectionStateListener", regCenterConnectionStateListener);
    }

    @Test
    public void assertStartAllListeners() {
        listenerManager.startAllListeners();
        verify(electionListenerManager).start();
        verify(shardingListenerManager).start();
        verify(failoverListenerManager).start();
        verify(monitorExecutionListenerManager).start();
        verify(shutdownListenerManager).start();
        verify(rescheduleListenerManager).start();
        verify(guaranteeListenerManager).start();
        verify(jobNodeStorage).addConnectionStateListener(regCenterConnectionStateListener);
    }
}
