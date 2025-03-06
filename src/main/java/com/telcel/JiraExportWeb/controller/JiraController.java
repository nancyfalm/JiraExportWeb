package com.telcel.JiraExportWeb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RequestMapping(path = "fachada/jira/")
public class JiraController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

//    @GetMapping("hola")
//    public String hola() {
//        return "Hola mundo";
//    }
}