package com.guavapay.model.mapper;

import com.guavapay.model.entity.Account;
import com.guavapay.model.entity.Role;
import com.guavapay.model.type.AccountState;
import com.guavapay.security.Principal;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrincipalMapper {

    @Mapping(target = "login", source = "mobile")
    @Mapping(target = "authorities", source = "account.roles", qualifiedByName = "mapAuthorities")
    @Mapping(target = "status", source = "account.state", qualifiedByName = "mapState")
    @Mapping(target = "fullName", source = "account", qualifiedByName = "mapFullName")
    Principal toPrincipal(Account account);

    @Named("mapAuthorities")
    default Collection<? extends GrantedAuthority> mapAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Named("mapState")
    default Integer mapState(AccountState accountState) {
        return accountState.ordinal();
    }

    @Named("mapFullName")
    default String mapFullName(Account account) {
        return Stream.of(account.getName(), account.getSurname())
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }
}
