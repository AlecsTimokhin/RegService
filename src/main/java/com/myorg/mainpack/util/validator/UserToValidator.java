package com.myorg.mainpack.util.validator;

import com.myorg.mainpack.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserToValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserDto.class;
    }


    @Override
    public void validate(Object obj, Errors errors) {

        UserDto userDto = ((UserDto) obj);

        if (userDto.getUsername() == null ||
                userDto.getUsername().length() < 3 ||
                userDto.getUsername().length() > 30) {
            errors.rejectValue("username", "error.user", "Логин пользователя должен быть от 3 до 30 символов");
        }
        else{
            if( userDto.getUsername().contains("<") || userDto.getUsername().contains(">") ){
                errors.rejectValue("username", "error.user", "Символы '<' и '>' запрещены для логина пользователя");
            }
        }

        if( userDto.getUpdatePass() != null ){

            if (userDto.getPassword() == null ||
                    userDto.getPassword().length() < 6 ||
                    userDto.getPassword().length() > 20) {
                errors.rejectValue("password", "error.user", "Пароль пользователя должен быть от 6 до 20 символов");
            }

            if( userDto.getPassword() != null && userDto.getPassword2() != null ){
                if( !userDto.getPassword().equals( userDto.getPassword2() ) ){
                    errors.rejectValue("password2", "error.user", "Пароли не совпадают");
                }
            }

        }

    }

}




