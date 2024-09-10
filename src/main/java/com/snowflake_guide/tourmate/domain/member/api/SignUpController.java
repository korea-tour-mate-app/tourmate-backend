package com.snowflake_guide.tourmate.domain.member.api;

import com.snowflake_guide.tourmate.domain.member.dto.SignUpRequestDTO;
import com.snowflake_guide.tourmate.domain.member.entity.Member;
import com.snowflake_guide.tourmate.domain.member.service.SignUpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SingUp API", description = "회원가입하는 API")
@RestController
@RequestMapping("/api/auth/signup")
public class SignUpController {

    private final SignUpService signUpService;

    @Autowired
    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping
    public ResponseEntity<String> signUp(@Validated @RequestBody SignUpRequestDTO signUpRequestDTO) {
        if (signUpService.existsByEmail(signUpRequestDTO.getEmail())) {
            return new ResponseEntity<>("중복된 이름입니다.", HttpStatus.BAD_REQUEST);
        }

        if (signUpService.existsByNickname(signUpRequestDTO.getEmail())) {
            return new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        Member member = signUpService.signUp(signUpRequestDTO);
        return new ResponseEntity<>("회원가입을 성공하였습니다.", HttpStatus.CREATED);
    }
}
