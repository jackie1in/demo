package com.github.silencecorner.kafka;

import java.util.Collections;
import java.util.List;

public class Common {
    public static final List<String> AT_MOST_ONCE = Collections.singletonList("AT_MOST_ONCE");
    public static final List<String> AT_LEAST_ONCE = Collections.singletonList("AT_LEAST_ONCE");
    public static final List<String> EXACTLY_ONCE_AND_TRANSACTION = Collections.singletonList("EXACTLY_ONCE_AND_TRANSACTION");
}
