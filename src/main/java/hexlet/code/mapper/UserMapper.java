package hexlet.code.mapper;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
    uses = { JsonNullableMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeMapping
    public void encryptCreatePassword(UserCreateDTO userCreateDTO) {
        var password = userCreateDTO.getPassword();
        userCreateDTO.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void encryptUpdatePassword(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getPassword() != null && userUpdateDTO.getPassword().isPresent()) {
            var password = userUpdateDTO.getPassword().get();
            userUpdateDTO.setPassword(JsonNullable.of(passwordEncoder.encode(password)));
        }
    }

    @Mapping(target = "password", ignore = true)
    public abstract User map(UserCreateDTO userCreateDTO);

    public abstract UserDTO map(User model);

    @Mapping(target = "password", ignore = true)
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
