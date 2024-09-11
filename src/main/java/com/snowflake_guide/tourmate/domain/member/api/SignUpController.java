package com.snowflake_guide.tourmate.domain.member.api;

import com.snowflake_guide.tourmate.domain.member.dto.SignUpRequestDTO;
import com.snowflake_guide.tourmate.domain.member.service.SignUpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "SignUp API", description = "회원가입하는 API")
@RestController
@RequestMapping("/api/auth/signup")
public class SignUpController {

    private final SignUpService signUpService;

    @Autowired
    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> signUp(@Validated @RequestBody SignUpRequestDTO signUpRequestDTO) {
        Map<String, String> response = new HashMap<>();

        if (signUpService.existsByEmail(signUpRequestDTO.getEmail())) {
            response.put("message", "중복된 이메일입니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (signUpService.existsByNickname(signUpRequestDTO.getNickname())) {
            response.put("message", "중복된 닉네임입니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        signUpService.signUp(signUpRequestDTO);
        response.put("message", "회원가입을 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
