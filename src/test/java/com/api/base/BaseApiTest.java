package com.api.base;

import com.api.framework.config.FrameworkConfig;
import com.api.framework.world.ScenarioWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Base class every API test extends + Spring boilerplate.
 */
@SpringJUnitConfig(FrameworkConfig.class)
public abstract class BaseApiTest {

    private static final Logger log = LoggerFactory.getLogger(BaseApiTest.class);

    @Autowired
    private ApplicationContext applicationContext;

    protected ScenarioWorld world;

    @BeforeEach
    void initWorld(TestInfo testInfo) {
        world = applicationContext.getBean(ScenarioWorld.class);
        log.info(">>> Starting: {}", testInfo.getDisplayName());
    }
}
