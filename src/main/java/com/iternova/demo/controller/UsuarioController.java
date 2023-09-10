package com.iternova.demo.controller;

import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Usuario;
import com.iternova.demo.service.OrdenService;
import com.iternova.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    BCryptPasswordEncoder passEncode =new BCryptPasswordEncoder();

    @GetMapping("/registro")
    public String create(){
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario){

        usuario.setTipo("USER");
        usuario.setPassword(passEncode.encode(usuario.getPassword()));
        usuarioService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }
    @GetMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, Model model){

        Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString()));
        System.out.println(user.get().getNombre());
        if(user.isPresent()){

            model.addAttribute("usuario", user);
            if(user.get().getTipo().equals("ADMIN")){
                System.out.println("el usuario es ADMIN");
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        } else {
            System.out.println("el usuario no existe");
        }

        return "redirect:/usuario/login";
    }

    @GetMapping("/compras")
    public String obtenerCompras(HttpSession session, Model model){
        model.addAttribute("sesion", session.getAttribute("idUsuario"));
        Usuario usuario =usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);

        model.addAttribute("ordenes", ordenes);
        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id , Model model, HttpSession session){

    Optional <Orden> orden = ordenService.findById(id);

    model.addAttribute("detalles", orden.get().getDetalle());
        //session
        model.addAttribute("sesion", session.getAttribute("isUsuario"));
        return "usuario/detallecompra";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session){
        session.removeAttribute("idUsuario");
        return"redirect:/";
    }
}
