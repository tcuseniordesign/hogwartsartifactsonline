package edu.tcu.cs.hogwartsartifactsonline.controller;

import edu.tcu.cs.hogwartsartifactsonline.controller.converter.UserDtoToUser;
import edu.tcu.cs.hogwartsartifactsonline.controller.converter.UserToUserDto;
import edu.tcu.cs.hogwartsartifactsonline.controller.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.Result;
import edu.tcu.cs.hogwartsartifactsonline.domain.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService service;

    private UserDtoToUser userDtoToUser; // used to convert userDto to user

    private UserToUserDto userToUserDto; // used to convert user to userDto

    public UserController(UserService service, UserDtoToUser userDtoToUser, UserToUserDto userToUserDto) {
        this.service = service;
        this.userDtoToUser = userDtoToUser;
        this.userToUserDto = userToUserDto;
    }

    @GetMapping
    public Result findAll() {
        List<HogwartsUser> foundHogwartsUsers = service.findAll();

        // Convert foundUsers to a list of UserDtos
        List<UserDto> list = foundHogwartsUsers.stream()
                .map(userToUserDto::convert)
                .collect(Collectors.toList());
        // Note that UserDto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Find All Success", list);
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        HogwartsUser foundHogwartsUser = service.findById(id);
        UserDto userDTO = userToUserDto.convert(foundHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDTO);
    }

    /**
     * We are not using UserDto, but User, since we require password!
     * @param newHogwartsUser
     * @return
     */
    @PostMapping
    public Result save(@Valid @RequestBody HogwartsUser newHogwartsUser) {
        service.save(newHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Save Success");
    }

    // We are not using this to update password, need another changePassword method in this class
    @PutMapping("/{id}")
    public Result update(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
        HogwartsUser updatedHogwartsUser = userDtoToUser.convert(userDto);
        service.update(id, updatedHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        service.deleteById(id);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
