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
import com.lafarleaf.planner.utils.TaskCodeGenerator;
import com.lafarleaf.planner.utils.Exceptions.TaskNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dfplanner/tasks")
public class UserTaskController {
    private final SimpleDateFormat dbDateFormat;
    private final TaskCodeGenerator taskCodeGenerator;

    private final UserService userService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;

    @Autowired
    public UserTaskController(SimpleDateFormat dbDateFormat, TaskCodeGenerator taskCodeGenerator,
            UserService userService, TaskService taskService, SubtaskService subtaskService) {
        this.dbDateFormat = dbDateFormat;
        this.taskCodeGenerator = taskCodeGenerator;
        this.userService = userService;
        this.taskService = taskService;
        this.subtaskService = subtaskService;
    }

    @GetMapping("gettask/")
    public List<Task> getTasks(@RequestParam("username") String username, @RequestParam("start") String start,
            @RequestParam("end") String end) {
        try {
            Date startDate = dbDateFormat.parse(start);
            Date endDate = dbDateFormat.parse(end);
            User user = userService.findByUsername(username);
            return taskService.findByUserAndDueDateBetween(user, startDate, endDate);
        } catch (UsernameNotFoundException unfe) {
            unfe.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return new ArrayList<>();
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
            task.setCode(taskCodeGenerator.generate());
            task.setTitle((String) params.get("title"));
            task.setUser(user);
            task.setDueDate(dueDate);
            taskService.add(task);

            if (params.containsKey("subtaskTitles")) {
                List<?> titles = new ArrayList<>((Collection<?>) params.get("subtaskTitles"));
                for (Object title : titles) {
                    Subtask subtask = new Subtask();
                    subtask.setTitle((String) title);
                    task.addSubtask(subtask);
                    subtaskService.add(subtask);
                }
            }

            responses.put("result", true);
        } catch (UsernameNotFoundException unfe) {
            responses.put("message", unfe.getMessage());
        } catch (ParseException pe) {
            responses.put("message", pe.getMessage());
        }

        return responses;
    }

    @PutMapping("updatetask/{code}")
    public Map<String, Object> updateTask(@PathVariable String code, @RequestBody Map<String, Object> params) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        try {
            Task task = taskService.findByCode(code);

            if (params.containsKey("title")) {
                task.setTitle((String) params.get("title"));
            }

            if (params.containsKey("dueDate")) {
                Date dueDate = dbDateFormat.parse((String) params.get("dueDate"));
                task.setDueDate(dueDate);
            }

            if (params.containsKey("isDone")) {
                task.setDone((boolean) params.get("isDone"));
            }

            if (params.containsKey("subtaskTitles")) {
                List<?> titles = new ArrayList<>((Collection<?>) params.get("subtaskTitles"));
                for (Object title : titles) {
                    Subtask subtask = new Subtask();
                    subtask.setTitle((String) title);
                    task.addSubtask(subtask);
                    subtaskService.add(subtask);
                }
            }

            taskService.add(task);
            responses.put("result", true);
        } catch (TaskNotFoundException tnfe) {
            responses.put("message", tnfe.getMessage());
        } catch (ParseException pe) {
            responses.put("message", pe.getMessage());
        }

        return responses;
    }

    @PutMapping("updatetask/{code}/{id}")
    public Map<String, Object> updateSubtask(@PathVariable String code, @PathVariable long id,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        try {
            Task task = taskService.findByCode(code);
            Subtask subtask = subtaskService.findByTaskAndId(task, id);

            if (params.containsKey("title")) {
                subtask.setTitle((String) params.get("title"));
            }

            if (params.containsKey("isDone")) {
                subtask.setDone((boolean) params.get("isDone"));
            }

            subtaskService.add(subtask);
            responses.put("result", true);
        } catch (TaskNotFoundException tnfe) {
            responses.put("message", tnfe.getMessage());
        }

        return responses;
    }

    @DeleteMapping("deletetask/{code}")
    public Map<String, Object> deleteTask(@PathVariable String code) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        try {
            Task task = taskService.findByCode(code);
            taskService.delete(task);
            responses.put("result", true);
        } catch (TaskNotFoundException tnfe) {
            responses.put("message", tnfe.getMessage());
        }

        return responses;
    }

    @DeleteMapping("deletetask/{code}/{id}")
    public Map<String, Object> deleteSubtask(@PathVariable String code, @PathVariable long id) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        try {
            Task task = taskService.findByCode(code);
            Subtask subtask = subtaskService.findByTaskAndId(task, id);
            subtaskService.delete(subtask);
            responses.put("result", true);
        } catch (TaskNotFoundException tnfe) {
            responses.put("message", tnfe.getMessage());
        }

        return responses;
    }
}
