package com.iternova.demo.service;

import com.iternova.demo.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    public HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
        if(optionalUser.isPresent()){
            session.setAttribute("idUsuario",optionalUser.get().getId());
            session.setAttribute("nombre", optionalUser.get().getNombre());

            Usuario user = optionalUser.get();

            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            return User.builder().
                    username(user.getNombre()).
                    password(bcrypt.encode(user.getPassword())).roles(user.getTipo()).build();
        }else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

    }
}
