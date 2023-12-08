package in.ineuron.restcontrollers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-app")
public class TestController {

    @GetMapping("/test")
    public String TestApi(){
        return "Application is ready for service...";
    }

}
