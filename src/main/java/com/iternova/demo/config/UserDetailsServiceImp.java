package com.iternova.demo.config;

import com.iternova.demo.model.Usuario;
import com.iternova.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    //ver
    @Autowired
    private UsuarioService usuarioService;

    //ver para encriptar y desencriptar
    @Autowired
    private BCryptPasswordEncoder bcrypt;
    @Autowired
    HttpSession session;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("buscando username");
        Optional<Usuario> optionalUser = usuarioService.findByEmail(email);

        if(optionalUser.isPresent()){
            System.out.println("usuario encontrado");
            session.setAttribute("idUsuario", optionalUser.get().getId());
            Usuario user = optionalUser.get();

            return User.builder()
                    .username(user.getNombre())
                    .password(user.getPassword())
                    .roles(user.getTipo())
                    .build();
        }
            else {
                throw new UsernameNotFoundException("usuario no encontrado");
        }
    }
}
