package com.lafarleaf.planner.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lafarleaf.planner.models.Subtask;
import com.lafarleaf.planner.models.Task;
import com.lafarleaf.planner.models.User;
import com.lafarleaf.planner.services.SubtaskService;
import com.lafarleaf.planner.services.TaskService;
import com.lafarleaf.planner.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dfplanner/tasks")
public class UserTaskController {
    private final SimpleDateFormat dbDateFormat;
    private final UserService userService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;

    @Autowired
    public UserTaskController(SimpleDateFormat dbDateFormat, UserService userService, TaskService taskService,
            SubtaskService subtaskService) {
        this.dbDateFormat = dbDateFormat;
        this.userService = userService;
        this.taskService = taskService;
        this.subtaskService = subtaskService;
    }

    @PostMapping("addtask")
    public Map<String, Object> addTask(@RequestBody Map<String, Object> params) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        if (!params.containsKey("username") || !params.containsKey("title") || !params.containsKey("dueDate")) {
            responses.put("message", "Invalid parameters passed");
            return responses;
        }

        try {
            User user = userService.findByUsername((String) params.get("username"));
            Date dueDate = dbDateFormat.parse((String) params.get("dueDate"));

            Task task = new Task();
            task.setTitle((String) params.get("title"));
            task.setUser(user);
            task.setDueDate(dueDate);
            taskService.add(task);

            if (params.containsKey("subtaskTitles")) {
                System.out.println("Has Subs...");
                List<?> titles = new ArrayList<>((Collection<?>) params.get("subtaskTitles"));
                for (Object title : titles) {
                    System.out.println("SUB: " + title);
                    Subtask subtask = new Subtask();
                    subtask.setTitle((String) title);
                    task.addSubtask(subtask);
                    subtaskService.add(subtask);
                }
            }

            for (Subtask s : task.getSubtaskList()) {
                System.out.println("ADDED>>> " + s.getTitle());
            }
            responses.put("result", true);
        } catch (UsernameNotFoundException unfe) {
            responses.put("message", unfe.getMessage());
        } catch (ParseException pe) {
            responses.put("message", pe.getMessage());
        }

        return responses;
    }
}
