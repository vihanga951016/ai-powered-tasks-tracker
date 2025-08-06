package com.flex.task_tracker.common.constants;

import java.util.Set;

public class SecurityConstants {

    public static final String SECRET_KEY = "Rhy6W28jBTJvXhRyVxte0zq25hgRsYNc5e1X1vp16eY8qT4W";

    public static final String[] EXCLUDED_PATHS = {
            "/user/login",
            "/user/register",
            "/user/logout"
    };
}
