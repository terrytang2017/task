package com.diwayou.task.event.type;

import com.diwayou.task.context.ExecutionType;
import com.diwayou.task.event.JobEvent;

import java.util.Date;
import java.util.UUID;

/**
 * 作业状态痕迹事件.
 *
 * @author caohao
 */
public final class JobStatusTraceEvent implements JobEvent {

    private final String jobName;
    private final String taskId;
    private final String slaveId;
    private final Source source;
    private final ExecutionType executionType;
    private final String shardingItems;
    private final State state;
    private final String message;
    private String id = UUID.randomUUID().toString();
    private String originalTaskId = "";
    private Date creationTime = new Date();

    public JobStatusTraceEvent(String jobName, String taskId, String slaveId, Source source, ExecutionType executionType, String shardingItems, State state, String message) {
        this.jobName = jobName;
        this.taskId = taskId;
        this.slaveId = slaveId;
        this.source = source;
        this.executionType = executionType;
        this.shardingItems = shardingItems;
        this.state = state;
        this.message = message;
    }

    public JobStatusTraceEvent(String id, String jobName, String originalTaskId, String taskId, String slaveId, Source source, ExecutionType executionType, String shardingItems, State state, String message, Date creationTime) {
        this.id = id;
        this.jobName = jobName;
        this.originalTaskId = originalTaskId;
        this.taskId = taskId;
        this.slaveId = slaveId;
        this.source = source;
        this.executionType = executionType;
        this.shardingItems = shardingItems;
        this.state = state;
        this.message = message;
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    public String getOriginalTaskId() {
        return originalTaskId;
    }

    public JobStatusTraceEvent setOriginalTaskId(String originalTaskId) {
        this.originalTaskId = originalTaskId;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getSlaveId() {
        return slaveId;
    }

    public Source getSource() {
        return source;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public String getShardingItems() {
        return shardingItems;
    }

    public State getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public enum State {
        TASK_STAGING, TASK_RUNNING, TASK_FINISHED, TASK_KILLED, TASK_LOST, TASK_FAILED, TASK_ERROR, TASK_DROPPED, TASK_GONE, TASK_GONE_BY_OPERATOR, TASK_UNREACHABLE, TASK_UNKNOWN
    }

    public enum Source {
        CLOUD_SCHEDULER, CLOUD_EXECUTOR, LITE_EXECUTOR
    }
}
