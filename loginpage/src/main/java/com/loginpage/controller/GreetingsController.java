package com.loginpage.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GreetingsController {

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

    @RequestMapping(value="/greet/{name}",method= RequestMethod.GET)
    public String  greetings(@PathVariable("name") String name) {
        return "Hello " + name;
    }

    @PostMapping("/newmsg")
    public String  newmsg(@RequestBody String msg) {
        System.out.println("New message is: " + msg);
        return "Hello " + msg;
    }
}

