package com.pertos.bring;

import com.pertos.bring.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void run() {
        ApplicationContext context = Application.run("com.pertos.bring");
    }
}