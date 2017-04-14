package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
//            .antMatchers("/user").permitAll()
            .antMatchers("/user/*").hasRole("USER")
            .antMatchers("/user/*/connections").hasRole("USER")
            .antMatchers("/user/*/follow/*").hasRole("USER")
            .antMatchers("/user/*/unfollow/*").hasRole("USER")          
            .antMatchers("/user/*/distance/*").hasRole("USER")
            .and()
            .formLogin()
            .permitAll().and().logout();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
    	auth.inMemoryAuthentication()
    		.withUser("user").password("password").roles("USER");
    }
}