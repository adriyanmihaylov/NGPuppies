package com.paymentsystem.ngpuppies.models.users;

import com.paymentsystem.ngpuppies.models.IpAddress;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @ValidUsername
    @Column(name = "Username")
    @Size(min = 4, max = 20)
    private String username;

    @Column(name = "Password")
    private String password;

    @OneToOne
    @JoinColumn(name = "AuthorityID")
    private Authority authority;

    @Column(name = "Enabled")
    private Boolean enabled;

    @Column(name = "LastPasswordResetDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;


    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "users_ip",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = {@JoinColumn(name = "AddressId")}
    )
    private Set<IpAddress> ipAddresses;

    public User() {

    }

    User(String username, String password,Authority authority) {
        setUsername(username);
        setPassword(password);
        setAuthority(authority);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Set<IpAddress> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(Set<IpAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public void addIpAddress(IpAddress ipAddress) {
        if (this.ipAddresses == null) {
            setIpAddresses(new HashSet<>());
        }

        ipAddresses.add(ipAddress);
    }

    public Collection< ? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("" + getAuthority().getName());

        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

}