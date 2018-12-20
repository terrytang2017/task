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

package com.diwayou.task.integrate.std.script;

import com.diwayou.task.api.script.ScriptJob;
import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.config.script.ScriptJobConfiguration;
import com.diwayou.task.fixture.util.ScriptElasticJobUtil;
import com.diwayou.task.integrate.AbstractBaseStdJobAutoInitTest;
import com.diwayou.task.integrate.WaitingUtils;
import com.diwayou.task.internal.config.LiteJobConfigurationGsonFactory;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ScriptElasticJobTest extends AbstractBaseStdJobAutoInitTest {

    public ScriptElasticJobTest() {
        super(ScriptJob.class);
    }

    @Test
    public void assertJobInit() throws IOException {
        ScriptElasticJobUtil.buildScriptCommandLine();
        WaitingUtils.waitingShortTime();
        String scriptCommandLine = ((ScriptJobConfiguration) getLiteJobConfig().getTypeConfig()).getScriptCommandLine();
        LiteJobConfiguration liteJobConfig = LiteJobConfigurationGsonFactory.fromJson(getRegCenter().get("/" + getJobName() + "/config"));
        assertThat(((ScriptJobConfiguration) liteJobConfig.getTypeConfig()).getScriptCommandLine(), is(scriptCommandLine));
    }
}
