package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

    boolean existsByUsername(String username);
}
