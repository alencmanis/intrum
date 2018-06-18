package com.intrum.lenc.model.util;

import com.intrum.lenc.model.Debt;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DebtUtil {
    public static boolean validateDueDate(Debt debt) {
        Calendar calendar = Calendar.getInstance();
        Calendar deptCaledar = Calendar.getInstance();
        calendar.setTime(new Date());
        deptCaledar.setTime(debt.getDueDate());
        if (calendar.compareTo(deptCaledar) >= 0) {
            return false;
        }
        return true;
    }

    public static boolean validateAmount(Debt debt) {
            BigDecimal amount = debt.getAmount();
            if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
                return false;
            }
        return true;
    }

}
