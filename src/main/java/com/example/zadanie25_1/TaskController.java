package com.example.zadanie25_1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final List<Task> tasks;

    public TaskController(TaskRepository taskRepository, List<Task> tasks) {
        this.taskRepository = taskRepository;
        this.tasks = tasks;
    }

    @GetMapping("/")
    public String home(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "home";
    }

    @PostMapping("/add")
    public String add(Model model,
                      @RequestParam String title,
                      @RequestParam String description,
                      @RequestParam TaskCategory category) {
        Task task = new Task(title, description, category, LocalDateTime.now(), false);
        Task savedTask = taskRepository.save(task);
        tasks.add(savedTask);
        model.addAttribute("taskToAdd", task);
        return "add";
    }

    @GetMapping("/archive")
    public String archive(Model model) {
        List<Task> allTasks = taskRepository.findAll();
        List<Task> doneTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.isDone()) {
                doneTasks.add(task);
            }
        }
        model.addAttribute("doneTasks", doneTasks);
        return "archive";
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(required = false) TaskCategory category) {
        List<Task> taskList = taskRepository.findAll();
        List<Task> notDoneTasks = new ArrayList<>();
        for (Task task : taskList) {
            if (!task.isDone()) {
                notDoneTasks.add(task);
            }
        }
        model.addAttribute("tasks", notDoneTasks);
        List<Task> tasksFoundByCategory;
        if (category != null) {
            tasksFoundByCategory = taskRepository.findByCategory(category);
        } else {
            tasksFoundByCategory = taskRepository.findAll();
        }
        model.addAttribute("taskList", tasksFoundByCategory);
        return "list";
    }

    @PostMapping("/done/{id}")
    public String taskDone(@PathVariable Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDone(true);
            taskRepository.save(task);
            return "redirect:/list";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/list/{id}/edit")
    public String showTaskEditForm(@PathVariable Long id, Model model){
        Optional<Task> taskToEditOptional = taskRepository.findById(id);
        if (taskToEditOptional.isPresent()) {
            Task task = taskToEditOptional.get();
            model.addAttribute("taskToEdit", task);
            return "edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/list/{id}/edit")
    public String editTask(@PathVariable Long id, Task task) {
        Task taskToEdit = taskRepository.findById(id).orElseThrow();
        taskToEdit.setTitle(task.getTitle());
        taskToEdit.setCategory(task.getCategory());
        taskToEdit.setDescription(task.getDescription());
        taskRepository.save(taskToEdit);
        return "redirect:/list";
    }
}