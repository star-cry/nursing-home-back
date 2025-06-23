package neo.careplus.repository;

import neo.careplus.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {

    // 인증코드 확인할 때 사용
    Optional<Email> findByEmailAndVerificationCode(String email, int code);

}
