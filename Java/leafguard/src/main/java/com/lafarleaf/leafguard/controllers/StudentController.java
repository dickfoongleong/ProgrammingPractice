package com.lafarleaf.leafguard.controllers;

import java.util.Arrays;
import java.util.List;

import com.lafarleaf.leafguard.models.Student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENT_LIST = Arrays.asList(new Student(1, "Dick Foong Leong"),
            new Student(2, "Ella Chen"), new Student(3, "Sinhchhinh Lor"));

    @GetMapping("")
    public List<Student> getStudentList() {
        return STUDENT_LIST;
    }

    @GetMapping("{studentId}")
    public Student getStudent(@PathVariable("studentId") int studentId) {
        return STUDENT_LIST.stream().filter(student -> studentId == student.getId()).findFirst()
                .orElseThrow(() -> new IllegalStateException("Student " + studentId + " does not exists"));
    }
}
