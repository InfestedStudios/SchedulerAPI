package org.infestedstudios.sheduler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScheduledTask {
    String id();
    boolean async() default false;
    String delay() default "0S"; // Default delay is 0 seconds
    String interval() default ""; // Recurring interval, e.g., "5M" for 5 minutes
}
