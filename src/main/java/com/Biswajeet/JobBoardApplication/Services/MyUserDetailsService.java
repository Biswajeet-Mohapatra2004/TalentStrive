package com.Biswajeet.JobBoardApplication.Services;

import com.Biswajeet.JobBoardApplication.Model.Employer;
import com.Biswajeet.JobBoardApplication.Model.UserDetailsImplementation;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users user=repo.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("User Not found! Error Code 404");
        }
        return new UserDetailsImplementation(user);
    }

    public boolean userExists(String username) {
        Users user= repo.findByUsername(username);
        if(user!=null){
            return true;
        }
        else{
            return false;
        }
    }
}
