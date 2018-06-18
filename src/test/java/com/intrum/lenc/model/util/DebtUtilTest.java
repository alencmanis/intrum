package com.intrum.lenc.model.util;

import com.intrum.lenc.model.Debt;
import org.assertj.core.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class DebtUtilTest {
    @Test
    public void validateDueDate() {
        boolean result = DebtUtil.validateDueDate(new Debt(new Date(), null, null));
        Assert.assertFalse(result);
    }

    @Test
    public void validateAmount() {
        boolean result = DebtUtil.validateAmount(new Debt(new Date(), null, null));
        Assert.assertFalse(result);
    }

}
