package edu.tcu.cs.hogwartsartifactsonline.controller.converter;

import edu.tcu.cs.hogwartsartifactsonline.controller.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUser implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setUsername(source.getUsername());
        hogwartsUser.setEnabled(source.isEnabled());
        hogwartsUser.setRoles(source.getRoles());
        return hogwartsUser;
    }
}
