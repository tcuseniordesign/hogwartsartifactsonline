package edu.tcu.cs.hogwartsartifactsonline.service;

import edu.tcu.cs.hogwartsartifactsonline.dao.UserDao;
import edu.tcu.cs.hogwartsartifactsonline.domain.MyUserPrincipal;
import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserDao userDao;
    private PasswordEncoder encoder;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public List<HogwartsUser> findAll() {
        List<HogwartsUser> list = new ArrayList<>();
        List<HogwartsUser> hogwartsUsers = userDao.findAll();
        // What will happen if we uncomment the following snippet?
        // Try to run test cases and also see H2 database tables
//        users.stream().map(user -> {
//            user.setPassword(null); // set password to null
//            return user;
//        }).forEach(list::add);
        hogwartsUsers.forEach(list::add);
        return list;
    }

    public HogwartsUser findById(Integer userId) {
        return userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public HogwartsUser save(HogwartsUser newHogwartsUser) {
        // encode plain password before saving to the DB
        newHogwartsUser.setPassword(encoder.encode(newHogwartsUser.getPassword()));
        return userDao.save(newHogwartsUser);
    }

    /**
     * We are not using this update to change user password!
     * @param userId
     * @param updatedHogwartsUser
     * @return
     */
    public HogwartsUser update(Integer userId, HogwartsUser updatedHogwartsUser) {
        HogwartsUser oldHogwartsUser = userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        oldHogwartsUser.setUsername(updatedHogwartsUser.getUsername());
        oldHogwartsUser.setEnabled(updatedHogwartsUser.isEnabled());
        oldHogwartsUser.setRoles(updatedHogwartsUser.getRoles());
        return userDao.save(oldHogwartsUser);
    }

    public void deleteById(Integer userId) {
        userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userDao.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Step 1, we need to find this user from DB
        HogwartsUser hogwartsUser = userDao.findByUsername(username);
        // Step 2, if the user does exist
        if (hogwartsUser == null) {
            throw new UsernameNotFoundException("username " + username + " is not found");
        }
        // Otherwise, wrap the returned user instance in a MyUserPrincipal instance
        return new MyUserPrincipal(hogwartsUser); // return the principal
    }

}
