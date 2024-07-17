package ru.kitayanochka.shop_inventory_system.user.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.kitayanochka.shop_inventory_system.exception.AlreadyExistsException;
import ru.kitayanochka.shop_inventory_system.exception.NotFoundException;
import ru.kitayanochka.shop_inventory_system.user.UserRepository;
import ru.kitayanochka.shop_inventory_system.user.dto.UserCreationDto;
import ru.kitayanochka.shop_inventory_system.user.dto.UserFilterDto;
import ru.kitayanochka.shop_inventory_system.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.kitayanochka.shop_inventory_system.exception.message.UserExceptionMessage.*;
import static ru.kitayanochka.shop_inventory_system.user.mapper.UserMapper.USER_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepo;

    @Override
    public User createUser(UserCreationDto dto) {
        User user = USER_MAPPER.mapToUser(dto);
        validateCreateUser(user);
        user = userRepo.save(user);
        log.info("User is successfully saved! {}", user);
        return user;
    }

    @Override
    public User getUser(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage(id)));
    }

    @Override
    public List<User> getUsers(UserFilterDto filter) {
        Pageable pageable = new PageRequest(0, filter.getSize(), Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return filter.getFrom();
            }
        };
        return userRepo.findAll(getSpecByParams(filter), pageable).getContent();
    }

    @Override
    public User updateUser(int id, UserCreationDto dto) {
        User oldUser = getUser(id);
        validateUpdateUser(oldUser, dto);
        oldUser = userRepo.save(USER_MAPPER.updateUser(oldUser, dto));
        log.info("User with id({}) is successfully updated! {}", id, oldUser);
        return oldUser;
    }

    @Override
    public void deleteUser(int id) {
        User user = getUser(id);
        userRepo.delete(user);
        log.info("User is successfully deleted! {}", user);
    }

    private void validateCreateUser(User user) {
        validateEmail(user.getEmail());
        validatePhone(user.getPhone());
    }

    private void validateUpdateUser(User oldUser, UserCreationDto updatedUser) {
        if (!oldUser.getEmail().equals(updatedUser.getEmail())) {
            validateEmail(updatedUser.getEmail());
        }
        if (!oldUser.getPhone().equals(updatedUser.getPhone())) {
            validatePhone(updatedUser.getPhone());
        }
    }

    private void validateEmail(String email) {
        if (userRepo.existsByEmail(email)) {
            throw new AlreadyExistsException(EMAIL_ALREADY_IN_USE.getMessage(email));
        }
    }

    private void validatePhone(String phone) {
        if (userRepo.existsByPhone(phone)) {
            throw new AlreadyExistsException(PHONE_ALREADY_IN_USE.getMessage(phone));
        }
    }

    Specification<User> getSpecByParams(UserFilterDto params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.getId() != null) {
                Predicate byId = cb.equal(root.get("id"), params.getId());
                predicates.add(byId);
            }
            if (params.getFirstName() != null && !params.getFirstName().isBlank()) {
                String firstName = params.getFirstName().toLowerCase().trim();
                Predicate byFirstName = cb.like(cb.lower(root.get("firstName")), firstName);
                predicates.add(byFirstName);
            }
            if (params.getLastName() != null && !params.getLastName().isBlank()) {
                String lastName = params.getLastName().toLowerCase().trim();
                Predicate byLastName = cb.like(cb.lower(root.get("lastName")), lastName);
                predicates.add(byLastName);
            }
            if (params.getPhone() != null && !params.getPhone().isBlank()) {
                String phone = params.getPhone().toLowerCase().trim();
                Predicate byPhone = cb.like(root.get("phone"), phone);
                predicates.add(byPhone);
            }
            if (params.getEmail() != null && !params.getEmail().isBlank()) {
                String email = params.getEmail().toLowerCase().trim();
                Predicate byEmail = cb.like(cb.lower(root.get("email")), email);
                predicates.add(byEmail);
            }
            if (params.getCity() != null && !params.getCity().isBlank()) {
                String city = params.getCity().toLowerCase().trim();
                Predicate byCity = cb.like(cb.lower(root.get("city")), city);
                predicates.add(byCity);
            }
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

}
