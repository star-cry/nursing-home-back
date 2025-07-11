package neo.careplus.repository;

import neo.careplus.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

        Optional<User> findById(Long userId);

        Optional<User> findByEmail(String email);
}
