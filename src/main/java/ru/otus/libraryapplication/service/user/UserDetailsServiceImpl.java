package ru.otus.libraryapplication.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.libraryapplication.domain.Role;
import ru.otus.libraryapplication.domain.User;
import ru.otus.libraryapplication.repositories.user.UserRepository;

import static org.springframework.security.core.userdetails.User.UserBuilder;
import static org.springframework.security.core.userdetails.User.withUsername;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            UserBuilder builder = withUsername(username);
            builder.password(user.getPassword());
            builder.roles(user.getRoles().stream()
                    .map(Role::getRole).toArray(String[]::new));
            return builder.build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
