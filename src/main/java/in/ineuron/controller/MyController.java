package in.ineuron.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping("api/test")
    public String testController(@RequestParam String name){

        System.out.println("RAm");
        System.out.println("RAm");

        return "Good morning "+name;
    }

}
