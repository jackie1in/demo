package com.silencecorner.jpa.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/appointment")
public class PathController {


    @GetMapping("/{**path}")
    public String forward(@PathVariable String path){
        return path;
    }


}