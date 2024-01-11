package ma.formation.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Client user";
    }

    @GetMapping("/admin/test")
    public String helloProtected() {
        return "Hello ADMIN user";
    }
}
