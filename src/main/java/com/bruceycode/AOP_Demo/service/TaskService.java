package com.bruceycode.AOP_Demo.service;

import com.bruceycode.AOP_Demo.entity.Task;
import com.bruceycode.AOP_Demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        task.setStatus(task.getStatus() != null ? task.getStatus() : Task.TaskStatus.TODO);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);
        task.setTitle(taskDetails.getTitle() != null ? taskDetails.getTitle() : task.getTitle());
        task.setDescription(taskDetails.getDescription() != null ? taskDetails.getDescription() : task.getDescription());
        task.setStatus(taskDetails.getStatus() != null ? taskDetails.getStatus() : task.getStatus());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}