package org.com.openmarket.users.application.gateway.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @GetMapping("/health")
    public String healthCheck() {
        return "USER SERVICE: OK";
    }
}
