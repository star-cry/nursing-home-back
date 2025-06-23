package neo.careplus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import neo.careplus.domain.Email;
import neo.careplus.domain.User;
import neo.careplus.error.CustomException;
import neo.careplus.error.ErrorCode;
import neo.careplus.repository.EmailRepository;
import neo.careplus.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SignupService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;


    //1. 이메일 인증->인증 성공시 user 생성
    public Long verifyCodeAndCreateUser(String email,int code){

        Email emailEntity = emailRepository
                .findByEmailAndVerificationCode(email,code)
                .orElseThrow(() ->  new CustomException(ErrorCode.VERIFICATION_FAILED));

        if (emailEntity.isVerified()) {
            throw new CustomException(ErrorCode.VERIFICATION_FAILED, "이미 인증된 코드입니다.");
        }

        if (emailEntity.getExpires_at().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }

        emailEntity.setVerified(true);

        User user = new User();
        user.setEmail(email);
        userRepository.save(user);

        emailEntity.setUser(user);

        return user.getId();

    }

    //2. 계정 설정
    public void setAccountInfo(Long userId, String password, String passwordConfirm){

        if (!password.equals(passwordConfirm)){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(password);
    }

    //3. 이름 등록
    public void setProfile(Long userId, String name){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setName(name);
    }

    public boolean isEmailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void sendVerificationCode(String email) {
        int code = new Random().nextInt(900000) + 100000;

        Email emailEntity = new Email();
        emailEntity.setCreate_at(LocalDateTime.now());
        emailEntity.setExpires_at(LocalDateTime.now().plusMinutes(5)); // 유효시간 5분 설정
        emailEntity.setEmail(email);
        emailEntity.setVerificationCode(code);
        emailEntity.setVerified(false);
        emailEntity.setUser(null); // 아직 user 생성 전

        emailRepository.save(emailEntity);

        emailSenderService.sendMessage(
                email,
                "회원가입 인증코드입니다",
                "인증코드: " + code + "\n10분 내에 입력해주세요."
        );

}}
