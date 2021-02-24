package ar.com.atsa.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import ar.com.atsa.security.AuthenticationTokenProcessingFilter;

/**
 * SecurityConfiguration.
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@ComponentScan("ar.com.atsa.security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationEntryPoint unauthorizedEntryPoint;

    @Qualifier("atsaAuthenticationProvider")
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter;


    @Override
    protected UserDetailsService userDetailsService() {
        return this.userDetailsService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProvider).userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
   }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and()
            .authorizeRequests()
            .anyRequest().permitAll()
	        .and()
	      	.addFilterBefore(authenticationTokenProcessingFilter, BasicAuthenticationFilter.class);

        //http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        /*http
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint);
         //       .and()
           // .addFilter(authenticationTokenProcessingFilter);
        http
            .authenticationProvider(authenticationProvider).
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().p
                .and().userDetailsService(userDetailsService):*/
        http
            .csrf().disable();
    }

   /* @Bean
    public DaoAuthenticationProvider customAuthenticationManagerBean() {

        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userDetailsService);
        dap.setPasswordEncoder(passwordEncoder);
        return dap;
        
    }*/

}
