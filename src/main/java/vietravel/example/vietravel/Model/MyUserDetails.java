package vietravel.example.vietravel.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private final User user;
    private final boolean enabled;

    public MyUserDetails(User user) {
        this.user = user;
        this.enabled = true;
    }

    public Long getId() {
        return user.getUserId();
    }

    @Override
    public String getUsername() { return user.getEmail(); }
    @Override
    public String getPassword() { return user.getPassword(); }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // create & return a List<GrantedAuthority> from roles
//        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        String roleName = user.getRole() != null ? user.getRole().name() : "USER";
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
