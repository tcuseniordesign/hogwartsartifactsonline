package edu.tcu.cs.hogwartsartifactsonline.controller.converter;

import edu.tcu.cs.hogwartsartifactsonline.controller.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements Converter<HogwartsUser, UserDto> {

    @Override
    public UserDto convert(HogwartsUser source) {
        final UserDto userDto = new UserDto();
        userDto.setId(source.getId());
        userDto.setUsername(source.getUsername());
        // we are not setting password in DTO
        userDto.setEnabled(source.isEnabled());
        userDto.setRoles(source.getRoles());
        return userDto;
    }

}
