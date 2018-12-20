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

package com.diwayou.task.executor;

import com.diwayou.task.api.ElasticJob;
import com.diwayou.task.api.dataflow.DataflowJob;
import com.diwayou.task.api.simple.SimpleJob;
import com.diwayou.task.exception.JobConfigurationException;
import com.diwayou.task.executor.type.DataflowJobExecutor;
import com.diwayou.task.executor.type.ScriptJobExecutor;
import com.diwayou.task.executor.type.SimpleJobExecutor;

/**
 * 作业执行器工厂.
 *
 * @author zhangliang
 */
public final class JobExecutorFactory {

    /**
     * 获取作业执行器.
     *
     * @param elasticJob 分布式弹性作业
     * @param jobFacade  作业内部服务门面服务
     * @return 作业执行器
     */
    @SuppressWarnings("unchecked")
    public static AbstractElasticJobExecutor getJobExecutor(final ElasticJob elasticJob, final JobFacade jobFacade) {
        if (null == elasticJob) {
            return new ScriptJobExecutor(jobFacade);
        }
        if (elasticJob instanceof SimpleJob) {
            return new SimpleJobExecutor((SimpleJob) elasticJob, jobFacade);
        }
        if (elasticJob instanceof DataflowJob) {
            return new DataflowJobExecutor((DataflowJob) elasticJob, jobFacade);
        }
        throw new JobConfigurationException("Cannot support job type '%s'", elasticJob.getClass().getCanonicalName());
    }
}
