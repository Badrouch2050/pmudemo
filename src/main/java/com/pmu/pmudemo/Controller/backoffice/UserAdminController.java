package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.domains.Role;
import com.pmu.pmudemo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backoffice/users")
public class UserAdminController {
    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeUserRole(id, Role.fromString(role));
        return ResponseEntity.ok().build();
    }
} 