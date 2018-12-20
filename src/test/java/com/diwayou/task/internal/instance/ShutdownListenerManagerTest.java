package com.diwayou.task.internal.instance;

import com.diwayou.task.api.strategy.JobInstance;
import com.diwayou.task.internal.schedule.JobRegistry;
import com.diwayou.task.internal.schedule.JobScheduleController;
import com.diwayou.task.internal.schedule.SchedulerFacade;
import com.diwayou.task.internal.storage.JobNodeStorage;
import com.diwayou.task.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unitils.util.ReflectionUtils;

import static org.mockito.Mockito.*;

public final class ShutdownListenerManagerTest {

    @Mock
    private CoordinatorRegistryCenter regCenter;

    @Mock
    private JobScheduleController jobScheduleController;

    @Mock
    private JobNodeStorage jobNodeStorage;

    @Mock
    private InstanceService instanceService;

    @Mock
    private SchedulerFacade schedulerFacade;

    private ShutdownListenerManager shutdownListenerManager;

    @Before
    public void setUp() throws NoSuchFieldException {
        JobRegistry.getInstance().addJobInstance("test_job", new JobInstance("127.0.0.1@-@0"));
        shutdownListenerManager = new ShutdownListenerManager(null, "test_job");
        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setFieldValue(shutdownListenerManager, "instanceService", instanceService);
        ReflectionUtils.setFieldValue(shutdownListenerManager, "schedulerFacade", schedulerFacade);
        ReflectionUtils.setFieldValue(shutdownListenerManager, shutdownListenerManager.getClass().getSuperclass().getDeclaredField("jobNodeStorage"), jobNodeStorage);
    }

    @Test
    public void assertStart() {
        shutdownListenerManager.start();
        verify(jobNodeStorage).addDataListener(ArgumentMatchers.<TreeCacheListener>any());
    }

    @Test
    public void assertIsShutdownAlready() {
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(schedulerFacade, times(0)).shutdownInstance();
    }

    @Test
    public void assertIsNotLocalInstancePath() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.2@-@0", Type.NODE_REMOVED, "");
        verify(schedulerFacade, times(0)).shutdownInstance();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertUpdateLocalInstancePath() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_UPDATED, "");
        verify(schedulerFacade, times(0)).shutdownInstance();
        JobRegistry.getInstance().shutdown("test_job");
    }

    @Test
    public void assertRemoveLocalInstancePathForPausedJob() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        when(jobScheduleController.isPaused()).thenReturn(true);
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(schedulerFacade, times(0)).shutdownInstance();
    }

    @Test
    public void assertRemoveLocalInstancePathForReconnectedRegistryCenter() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        when(instanceService.isLocalJobInstanceExisted()).thenReturn(true);
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(schedulerFacade, times(0)).shutdownInstance();
    }

    @Test
    public void assertRemoveLocalInstancePath() {
        JobRegistry.getInstance().registerJob("test_job", jobScheduleController, regCenter);
        shutdownListenerManager.new InstanceShutdownStatusJobListener().dataChanged("/test_job/instances/127.0.0.1@-@0", Type.NODE_REMOVED, "");
        verify(schedulerFacade).shutdownInstance();
    }
}
