package com.example.springauthentication1.appuser;

import com.example.springauthentication1.registration.token.ConfirmationToken;
import com.example.springauthentication1.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
//for finding users when they try to log in while an interface for spring security is implemented
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String singUpUser(AppUser appUser) {
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if(userExists) {
            throw new IllegalStateException("email already taken");
        }


        String f_name = appUser.getFirstname();
        String l_name = appUser.getLastName();

        // new variable created to retrieve the encrypted password input by the user
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        // setting the user's password to the retrieved password
        appUser.setPassword(encodedPassword);
        appUser.setFirstName(f_name);
        appUser.setLastName(l_name);

        //persisting user to the database
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        //Send confirmation token
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: SEND EMAIL
        return token;
    }
}
