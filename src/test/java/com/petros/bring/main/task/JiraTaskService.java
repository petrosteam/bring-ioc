package com.petros.bring.main.task;

import com.petros.bring.annotations.Component;
import com.petros.bring.annotations.Primary;

@Primary
@Component
public class JiraTaskService implements TaskService {
    @Override
    public String getTask() {
        return "primary jira task";
    }
}
