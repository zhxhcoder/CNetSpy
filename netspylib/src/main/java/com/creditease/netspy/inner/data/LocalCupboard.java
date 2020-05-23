package com.creditease.netspy.inner.data;

import com.creditease.netspy.inner.cupboard.Cupboard;
import com.creditease.netspy.inner.cupboard.CupboardBuilder;

public class LocalCupboard {
    private static Cupboard cupboard;

    private LocalCupboard() {
    }

    static {
        getInstance().register(HttpTransaction.class);
    }

    public static Cupboard getInstance() {
        if (cupboard == null) {
            cupboard = new CupboardBuilder().build();
        }
        return cupboard;
    }

    public static Cupboard getAnnotatedInstance() {
        return new CupboardBuilder(getInstance())
            .useAnnotations()
            .build();
    }
}
