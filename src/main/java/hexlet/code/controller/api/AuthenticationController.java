package hexlet.code.controller.api;

import hexlet.code.dto.AuthenticationRequestDTO;
import hexlet.code.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> create(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        var authentication = new UsernamePasswordAuthenticationToken(
            authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword());

        authenticationManager.authenticate(authentication);

        var token = jwtUtils.generateToken(authenticationRequestDTO.getUsername());

        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(token);
    }
}
