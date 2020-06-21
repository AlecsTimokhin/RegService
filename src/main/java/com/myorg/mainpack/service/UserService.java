package com.myorg.mainpack.service;

import com.myorg.mainpack.dto.RestResponce;
import com.myorg.mainpack.dto.UserDto;
import com.myorg.mainpack.exception.types.ConflictException;
import com.myorg.mainpack.model.User;
import com.myorg.mainpack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Value("${USER_NOT_FOUND}")
    private String USER_NOT_FOUND;

    @Value("${sameUserIsExists}")
    private String sameUserIsExists;

    private UserRepository userRepository;
    public UserRepository getUserRepository() { return userRepository; }
    @Autowired
    public void setUserRepository(UserRepository userRepository) { this.userRepository = userRepository; }

    private PasswordEncoder passwordEncoder;
    public PasswordEncoder getPasswordEncoder() { return passwordEncoder; }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) { this.passwordEncoder = passwordEncoder; }



    @Override
    @Cacheable("users")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;

        user = userRepository.findByUsername(username);

        if( user == null ){
            //throw new UsernameNotFoundException("Пользователь не существует! Код ошибки: 3");
            //throw new BadCredentialsException("Пользователь не существует! Код ошибки: 3");
        }

        return user;

    }


    @Cacheable("users")
    public List<UserDto> getAll(){

        List<UserDto> userDtoList = new ArrayList<>();

        List<User> userList =  userRepository.findAll();

        for( User user : userList ){
            userDtoList.add( new UserDto(user) );
        }

        return  userDtoList;

    }


    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(UserDto userDto){

        String STR = "";

        User userFromDB = userRepository.findByUsername(userDto.getUsername());
        if (userFromDB != null) {
            STR = STR + sameUserIsExists + "<br>";
        }

        if ("".equals(STR)) {
            userDto.setActive(true);
            userDto.setId(null);
            userDto.setBalance(0);
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

            userRepository.save( new User(userDto) );
        }
        else {
            throw new ConflictException( new RestResponce(STR + "Код ошибки: 1", "error", 1) );
        }

    }


    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteDocument(Long id) {
        return userRepository.delete(id);
    }


}
