package com.simon.simonssecureapi.repository;

import com.simon.simonssecureapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    boolean existsByDateOfBirth(String dateOfBirth);
    boolean existsByDateOfBirthAndIdNot(String dateOfBirth, Long id);
}
