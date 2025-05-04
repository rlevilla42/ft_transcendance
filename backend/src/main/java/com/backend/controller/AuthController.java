package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.backend.dto.LoginDto;
import com.backend.dto.RegisterDto;
import com.backend.entity.User;
import com.backend.service.UserService;

@Controller //⬅️ Déclare cette classe comme un contrôleur Spring MVC (prêt à gérer des requêtes HTTP)
@RequestMapping("/auth") // ⬅️ Injecte automatiquement UserService via le constructeur. Nécessaire ici car pas de Lombok.
public class AuthController {
    private final UserService userService;

    @Autowired // ⬅️ Injecte automatiquement UserService via le constructeur. Nécessaire ici car pas de Lombok.
    public AuthController(UserService userService) {
        this.userService = userService;
    };

    @PostMapping("/register") // ⬅️ Route POST vers "/auth/register"
    @ResponseBody // ⬅️ Indique que la réponse est un objet brut (JSON) et **pas** une vue HTML
    public ResponseEntity<User> register(@RequestBody RegisterDto registerDto) {
        
        User registeredUser = userService.register(registerDto);
        return ResponseEntity.ok(registeredUser);
    };

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<User> login(@RequestBody LoginDto loginDto) {
        User loginUser = userService.login(loginDto);
        return ResponseEntity.ok(loginUser);
    }
}
