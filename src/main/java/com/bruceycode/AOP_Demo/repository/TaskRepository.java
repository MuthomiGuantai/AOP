package com.bruceycode.AOP_Demo.repository;

import com.bruceycode.AOP_Demo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
