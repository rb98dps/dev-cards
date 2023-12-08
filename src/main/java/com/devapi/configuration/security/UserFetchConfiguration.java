package com.devapi.configuration.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class UserFetchConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsManager databaseUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("select email,password,is_active from user where email =?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id,role_name FROM users_roles INNER JOIN user " +
                "ON user.id = users_roles.user_id INNER JOIN role ON users_roles.role_Id = role.id where email =?");
        return userDetailsManager;
    }
   /*@Bean
    public InMemoryUserDetailsManager users() {
        UserDetails emp = User.builder()
                .username("user")
                .password("{noop}Admin@123")
                .roles("EMPLOYEE")
                .build();
        UserDetails man = User.builder()
                .username("manager")
                .password("{noop}Admin@123")
                .roles("MANAGER", "EMPLOYEE")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}Admin@123")
                .roles("EMPLOYEE","MANAGER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(emp, admin,man);

    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(HttpMethod.POST, "/api/user/**").hasRole("ADMIN")  //equivalent to ROLE_ADMIN in role table
                .requestMatchers(HttpMethod.POST, "/api/user/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAuthority("ADMIN")
                .anyRequest().authenticated());

        httpSecurity.httpBasic(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();

    }
}
