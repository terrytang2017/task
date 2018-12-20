package com.diwayou.task.internal.listener;

import com.diwayou.task.api.strategy.JobInstance;
import com.diwayou.task.internal.instance.InstanceService;
import com.diwayou.task.internal.schedule.JobRegistry;
import com.diwayou.task.internal.schedule.JobScheduleController;
import com.diwayou.task.internal.server.ServerService;
import com.diwayou.task.internal.sharding.ExecutionService;
import com.diwayou.task.internal.sharding.ShardingService;
import com.diwayou.task.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.state.ConnectionState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.util.ReflectionUtils;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public final class RegistryCenterConnectionStateListenerTest {

    @Mock
    private CoordinatorRegistryCenter regCenter;

    @Mock
    private ServerService serverService;

    @Mock
    private InstanceService instanceService;

    @Mock
    private ShardingService shardingService;

    @Mock
    private ExecutionService executionService;

    @Mock
    private JobScheduleController jobScheduleController;

    private RegistryCenterConnectionStateListener regCenterConnectionStateListener;

    @Before
    public void setUp() throws NoSuchFieldException {
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
        regCenterConnectionStateListener = new RegistryCenterConnectionStateListener(null, "test_job");
        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setFieldValue(regCenterConnectionStateListener, "serverService", serverService);
        ReflectionUtils.setFieldValue(regCenterConnectionStateListener, "instanceService", instanceService);
        ReflectionUtils.setFieldValue(regCenterConnectionStateListener, "shardingService", shardingService);
        ReflectionUtils.setFieldValue(regCenterConnectionStateListener, "executionService", executionService);
    }

    @Test
    public void assertConnectionLostListenerWhenConnectionStateIsLost() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        regCenterConnectionStateListener.stateChanged(null, ConnectionState.LOST);
        verify(jobScheduleController).pauseJob();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertConnectionLostListenerWhenConnectionStateIsLostButIsShutdown() {
        regCenterConnectionStateListener.stateChanged(null, ConnectionState.LOST);
        verify(jobScheduleController, times(0)).pauseJob();
        verify(jobScheduleController, times(0)).resumeJob();
    }

    @Test
    public void assertConnectionLostListenerWhenConnectionStateIsReconnected() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        when(shardingService.getLocalShardingItems()).thenReturn(Arrays.asList(0, 1));
        when(serverService.isEnableServer("127.0.0.1")).thenReturn(true);
        regCenterConnectionStateListener.stateChanged(null, ConnectionState.RECONNECTED);
        verify(serverService).persistOnline(true);
        verify(executionService).clearRunningInfo(Arrays.asList(0, 1));
        verify(jobScheduleController).resumeJob();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertConnectionLostListenerWhenConnectionStateIsReconnectedButIsShutdown() {
        when(shardingService.getLocalShardingItems()).thenReturn(Arrays.asList(0, 1));
        when(serverService.isEnableServer("127.0.0.1")).thenReturn(true);
        regCenterConnectionStateListener.stateChanged(null, ConnectionState.RECONNECTED);
        verify(jobScheduleController, times(0)).pauseJob();
        verify(jobScheduleController, times(0)).resumeJob();
    }

    @Test
    public void assertConnectionLostListenerWhenConnectionStateIsOther() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        regCenterConnectionStateListener.stateChanged(null, ConnectionState.CONNECTED);
        verify(jobScheduleController, times(0)).pauseJob();
        verify(jobScheduleController, times(0)).resumeJob();
        JobRegistry.getInstance().shutdown("test_job");
    }
}