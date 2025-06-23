package neo.careplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import neo.careplus.error.CustomException;
import neo.careplus.error.ErrorCode;
import neo.careplus.repository.signup.AccountSetupDto;
import neo.careplus.repository.signup.EmailVerificationDto;
import neo.careplus.repository.signup.ProfileDto;
import neo.careplus.service.SignupService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;


    @Operation(summary = "이메일 인증코드 발송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증코드 전송 성공",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일입니다.",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class)))
    })
    @PostMapping("/send-code")
    public ResponseEntity<neo.careplus.error.ApiResponse<Void>> sendCode(@RequestParam String email){

        if (signupService.isEmailDuplicated(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        signupService.sendVerificationCode(email);
        return ResponseEntity.ok(neo.careplus.error.ApiResponse.success(null));
    }


    @Operation(summary = "이메일 인증 확인 및 유저 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "인증 실패 또는 만료",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class)))
    })
    @PostMapping("/verify-code")
    public ResponseEntity<neo.careplus.error.ApiResponse<Long>> verifyCode(@RequestBody EmailVerificationDto verificationDto){

        Long userId = signupService.verifyCodeAndCreateUser(verificationDto.getEmail(), verificationDto.getVerificationCode());
        return ResponseEntity.ok(neo.careplus.error.ApiResponse.success(userId));

    }


    @Operation(summary = "계정 정보 설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계정 생성 완료",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class)))
    })
    @PostMapping("/set-account")
    public ResponseEntity<neo.careplus.error.ApiResponse<Void>> setAccount(@RequestBody AccountSetupDto accountSetupDto){

        signupService.setAccountInfo(accountSetupDto.getUserId(),accountSetupDto.getPassword(),accountSetupDto.getPasswordConfirm());
        return ResponseEntity.ok(neo.careplus.error.ApiResponse.success(null));

    }


    @Operation(summary = "프로필 이름 설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이름 저장 완료",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = neo.careplus.error.ApiResponse.class)))
    })
    @PostMapping("/set-profile")
    public ResponseEntity<neo.careplus.error.ApiResponse<Void>> setProfile(@RequestBody ProfileDto profileDto){

        signupService.setProfile(profileDto.getUserId(),profileDto.getName());
        return ResponseEntity.ok(neo.careplus.error.ApiResponse.success(null));

    }
}
