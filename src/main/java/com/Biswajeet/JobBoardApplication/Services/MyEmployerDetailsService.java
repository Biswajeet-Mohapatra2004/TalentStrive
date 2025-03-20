package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.Model.Employer;
import com.Biswajeet.JobBoardApplication.Model.EmployerDetailsImplementation;
import com.Biswajeet.JobBoardApplication.Repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyEmployerDetailsService implements UserDetailsService {

    @Autowired
    private EmployerRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employer employer_details=repo.findByUsername(username);
        if(employer_details==null){
            throw new UsernameNotFoundException("Employer not found! Error 404");
        }
        return new EmployerDetailsImplementation(employer_details);
    }

    public boolean employerExist(String username) {
        Employer employer= repo.findByUsername(username);
        if(employer!=null){
            return true;
        }
        else{
            return false;
        }
    }
}
