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

package com.diwayou.task.event.rdb;

import com.diwayou.task.event.JobEventConfiguration;
import com.diwayou.task.event.JobEventListener;
import com.diwayou.task.event.JobEventListenerConfigurationException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * 作业数据库事件配置.
 *
 * @author caohao
 */
public final class JobEventRdbConfiguration extends JobEventRdbIdentity implements JobEventConfiguration, Serializable {

    private final transient DataSource dataSource;

    public JobEventRdbConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public JobEventListener createJobEventListener() throws JobEventListenerConfigurationException {
        try {
            return new JobEventRdbListener(dataSource);
        } catch (final SQLException ex) {
            throw new JobEventListenerConfigurationException(ex);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
