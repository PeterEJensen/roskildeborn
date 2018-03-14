package com.peterpc.controller;

import com.peterpc.config.Student;
import com.peterpc.config.UserServiceImpl;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class DefaultController {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(path = "/calendar", method = RequestMethod.GET)
    String calendar(Model model) {
        return "calendar";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        log.info("index action called...");
        model.addAttribute("students", userService.fetchAllUsers());

        log.info("index action ended...");
        return "admin";
    }


    @GetMapping("/create")
    public String create(Model model) {
        log.info("create action called...");
        model.addAttribute("student", new Student());
        //model.addAttribute("students", userService.fetchAllUsers());
        return "create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Student student, Model model) {
        log.info("create post action called...");
        String index = Integer.toString(userService.fetchAllUsers().size());
        student.setStudentId(Integer.parseInt(index));
        userService.fetchAllUsers().add(student);
        model.addAttribute("students", userService.fetchAllUsers());
        return "redirect:/";
    }


    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        log.info("details action called...");

        //this.id = id;
        //this.model = model;
        Student stud = null;
        for (Student student : userService.fetchAllUsers()) {
            if (student.getStudentId() == id) {
                stud = student;
                break;
            }
        }
        model.addAttribute("student", stud);
        model.addAttribute("students", userService.fetchAllUsers());
        return "details";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        log.info("delete  action called...");
        model.addAttribute("student", userService.fetchAllUsers().get(id));
        model.addAttribute("students", userService.fetchAllUsers());
        return "delete";
    }

    @PostMapping("delete")
    public String delete(@ModelAttribute Student student, Model model) {
        log.info("delete post action called...");
        int id = student.getStudentId();
        userService.fetchAllUsers().remove(student.getStudentId());

        leftShiftId(userService.fetchAllUsers(), id);

        model.addAttribute("students", userService.fetchAllUsers());
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        log.info("edit action called...");
        //public String edit(@PathVariable ("ID") int id, Model model){
        //this.id = id;
        //this.model = model;

        model.addAttribute("student", userService.fetchAllUsers().get(id));
        model.addAttribute("students", userService.fetchAllUsers());
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Student student, Model model) {
        log.info("edit post action called...");
        for (int i = 0; i < userService.fetchAllUsers().size(); i++) {
            if (student.getStudentId() == userService.fetchAllUsers().get(i).getStudentId()) {
                userService.fetchAllUsers().set(i, student);
                //students.remove(i);
                //students.add(i, student);
            }
        }
        model.addAttribute("students", userService.fetchAllUsers());
        return "redirect:/";
    }

    private void leftShiftId(ArrayList<Student> list, int id) {
        log.info("leftshift method called...");
        for (int i = id; i < list.size(); i++) {
            Student student = list.get(i);
            student.setStudentId(student.getStudentId() - 1);
        }
    }


    @GetMapping("/")
    public String home1() {
        return "/home";
    }

  /*  @GetMapping("/home")
    public String home() {
        return "/home";
    }*/


    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("student", new Student());
        return "/user";
    }


    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}
