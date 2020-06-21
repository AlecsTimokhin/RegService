package com.myorg.mainpack.web.controllerRest;

import com.myorg.mainpack.dto.RestResponce;
import com.myorg.mainpack.dto.UserDto;
import com.myorg.mainpack.exception.types.BadValidateException;
import com.myorg.mainpack.exception.types.ConflictException;
import com.myorg.mainpack.exception.types.NoAccessException;
import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
import com.myorg.mainpack.service.UserService;
import com.myorg.mainpack.util.validator.CommonValidationUtil;
import com.myorg.mainpack.util.validator.UserToValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/rest/users")
public class UserRestController {

    @Value("${noAccsses}")
    private String noAccsses;

    @Value("${roleError}")
    private String roleError;

    @Value("${badFields}")
    private String badFields;

    @Value("${goodReg}")
    private String goodReg;

    @Value("${goodDeleteUser}")
    private String goodDeleteUser;

    @Value("${badDeleteUser}")
    private String badDeleteUser;

    private UserService userService;
    public UserService getUserService() { return userService; }
    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    UserToValidator userToValidator;
    public UserToValidator getUserToValidator() { return userToValidator; }
    @Autowired
    public void setUserToValidator(UserToValidator userToValidator) { this.userToValidator = userToValidator; }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userToValidator);
    }



    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(HttpServletRequest request){
        return ResponseEntity.status(200).body( userService.getAll() );
    }


    @PostMapping
    public ResponseEntity<RestResponce> addNewUser(@Valid UserDto userDto,
                                                   BindingResult bindingResult,
                                                   HttpServletRequest request){

        // Если пользователь залогинен, но не админ
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        if( currentUser != null ){
            if( currentUser.getId() != null ){
                if( currentUser.getId() > 0 && !currentUser.getRoles().contains(Role.ADMIN) ) {
                    throw new NoAccessException( new RestResponce(noAccsses, "error") );
                }
            }
        }

        if( currentUser != null && currentUser.getRoles().contains(Role.ADMIN) ){
            if( userDto.getRoles() == null || userDto.getRoles().size() == 0 ){
                bindingResult.rejectValue("roles", "error.user", roleError );
            }
        }
        else {
            userDto.setRoles( Set.of(Role.USER) );
        }

        // Проверка @Valid User на правильность
        HashMap<String, String> params = new HashMap<>();
        if( !CommonValidationUtil.validFields(bindingResult, params) ){
            throw new BadValidateException( new RestResponce(badFields, "error", params) ); }

        userService.save( userDto );

        log.info("Новый пользователь успешно зарегистрирован url:( " + request.getRequestURL() + " )");
        return ResponseEntity.status(201).body( new RestResponce(goodReg + "<br> Код ошибки: 0", "good", 0) );

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RestResponce> deleteDocument(@PathVariable("id") Long id) {

        if( userService.deleteDocument(id) ){
            return ResponseEntity.status(200).body( new RestResponce(goodDeleteUser, "good") );
        }
        else{
            return ResponseEntity.status(200).body( new RestResponce(badDeleteUser, "error") );
        }

    }


}
