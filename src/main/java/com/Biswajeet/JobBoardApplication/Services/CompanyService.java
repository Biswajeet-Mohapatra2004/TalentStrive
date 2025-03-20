package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.Model.Company;
import com.Biswajeet.JobBoardApplication.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository repo;

    public void registerCompany(Company company) {
        repo.save(company);
    }

    public List<Company> getAllCompanies() {
        return repo.findAll();
    }

    public void saveAll(List<Company> companies) {
        repo.saveAll(companies);
    }
}
