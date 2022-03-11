package com.sim.landlord.landLordJavaFx.view;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/11 14:33
 * @Desc:
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLView {
    String value() default "";

    String[] css() default {};

    String bundle() default "";

    String title() default "";

    String stageStyle() default "UTILITY";
}