package hexlet.code.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    ResponseEntity<String> welcome() {
        var welcome = "Welcome to Spring";

        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(welcome);
    }
}
