package com.snowflake_guide.tourmate.domain.member.api;

import com.snowflake_guide.tourmate.domain.member.dto.SignInRequestDTO;
import com.snowflake_guide.tourmate.domain.member.dto.SignInResponseDTO;
import com.snowflake_guide.tourmate.domain.member.service.SignInService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SignIn API", description = "자체 로그인하는 API")
@RestController
@RequestMapping("/api/auth/login")
public class SignInController {

    private final SignInService signInService;

    @Autowired
    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @PostMapping
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        SignInResponseDTO signInResponseDTO = signInService.login(signInRequestDTO);
        return ResponseEntity.ok(signInResponseDTO);
    }
}
