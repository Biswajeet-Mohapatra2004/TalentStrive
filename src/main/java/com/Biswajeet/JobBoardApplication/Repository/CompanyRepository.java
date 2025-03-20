package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

}
