package com.itm.space.notificationservice;

import com.itm.space.itmplatformcommonmodels.util.JsonParserUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {
    @Spy
    protected JsonParserUtil jsonParserUtil;
}
