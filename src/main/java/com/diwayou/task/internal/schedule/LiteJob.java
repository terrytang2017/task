package com.diwayou.task.internal.schedule;

import com.diwayou.task.api.ElasticJob;
import com.diwayou.task.executor.JobExecutorFactory;
import com.diwayou.task.executor.JobFacade;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Lite调度作业.
 *
 * @author zhangliang
 */
public final class LiteJob implements Job {

    private ElasticJob elasticJob;

    private JobFacade jobFacade;

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        JobExecutorFactory.getJobExecutor(elasticJob, jobFacade).execute();
    }

    public ElasticJob getElasticJob() {
        return elasticJob;
    }

    public void setElasticJob(ElasticJob elasticJob) {
        this.elasticJob = elasticJob;
    }

    public JobFacade getJobFacade() {
        return jobFacade;
    }

    public void setJobFacade(JobFacade jobFacade) {
        this.jobFacade = jobFacade;
    }
}
