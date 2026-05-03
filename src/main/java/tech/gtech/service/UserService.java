package tech.gtech.service;

import io.vertx.ext.auth.User;
import tech.gtech.domain.Users;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import tech.gtech.exceptcions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Transactional
    public Users createUser(Users users){
        Users.persist(users);
        return users;
    }


    public List<Users> findAllUsers(Integer page, Integer size) {
        return Users.findAll()
                .page(page, size)
                .list();
    }

    public Users findUserById(UUID id) {
        return (Users) Users.findByIdOptional(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public Object updateUser(UUID id, Users user) {
        Users updatedUser = findUserById(id);
        updatedUser.username = user.username;
        updatedUser.email = user.email;
        Users.persist(updatedUser);
        return updatedUser;
    }
    @Transactional
    public void deleteUserById(UUID id) {
        findUserById(id);
        Users.deleteById(id);
    }
}
