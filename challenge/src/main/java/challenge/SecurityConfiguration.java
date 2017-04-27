package challenge;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/h2-console/*").permitAll()
            .antMatchers("/user").permitAll()          
            .antMatchers("/user/connections").permitAll()
            .antMatchers("/user/follow").permitAll()
            .antMatchers("/user/unfollow").permitAll()          
            .antMatchers("/user/distance").permitAll();
            
    http.csrf().disable();
    http.headers().frameOptions().disable();
    }
}