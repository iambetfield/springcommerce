package com.iternova.demo.controller;


import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Producto;
import com.iternova.demo.model.Usuario;
import com.iternova.demo.service.OrdenService;
import com.iternova.demo.service.ProductoService;
import com.iternova.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @GetMapping("")
    public String home(Model model){
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        return "admin/home.html";
    }
    @GetMapping("/usuarios")
    public String usuarios(Model model){
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);

        return "admin/usuarios";
    }

    @GetMapping("/ordenes")
    public String ordenes(Model model){
      model.addAttribute("ordenes", ordenService.findAll());
        return "admin/ordenes";
    }

    @GetMapping("/detalle/{id}")
    public String detalles(Model model, @PathVariable Integer id){
        Orden orden = ordenService.findById(id).get();
        // la clase orden tiene una lista de Detalles, entonces traigo el objeto y luego uso su lista
        model.addAttribute("detalles", orden.getDetalle());

        return "admin/detalleorden";
    }
}
