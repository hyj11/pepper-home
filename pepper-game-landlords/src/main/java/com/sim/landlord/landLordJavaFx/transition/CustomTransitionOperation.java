package com.sim.landlord.landLordJavaFx.transition;

import javafx.animation.Animation;

public interface CustomTransitionOperation {
    double defaultSeconds = 0.4;
    Animation build();
    void play();
}
