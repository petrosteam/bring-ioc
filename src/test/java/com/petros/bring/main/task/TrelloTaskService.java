package com.petros.bring.main.task;

import com.petros.bring.annotations.Component;

@Component
public class TrelloTaskService implements TaskService {
    @Override
    public String getTask() {
        return "trello task";
    }
}
