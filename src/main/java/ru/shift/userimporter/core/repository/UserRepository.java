package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shift.userimporter.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
