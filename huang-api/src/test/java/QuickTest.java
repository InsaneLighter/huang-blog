import com.alibaba.druid.support.json.JSONUtils;
import com.huang.Application;
import com.huang.controller.admin.PostController;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

/**
 * @Time 2022-04-14 11:38
 * Created by Huang
 * className: QuickTest
 * Description:
 */
@SpringBootTest(classes = Application.class)
@Slf4j
public class QuickTest {
    @Autowired
    PostController controller;

    @Test
    public void test(){
    }
}
