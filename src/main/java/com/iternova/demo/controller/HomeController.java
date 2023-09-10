package com.iternova.demo.controller;

import com.iternova.demo.model.DetalleOrden;
import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Producto;
import com.iternova.demo.model.Usuario;
import com.iternova.demo.service.DetalleOrdenService;
import com.iternova.demo.service.OrdenService;
import com.iternova.demo.service.ProductoService;
import com.iternova.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private DetalleOrdenService detalleOrdenService;

    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    Orden orden = new Orden();



    @GetMapping("")
    public String home(Model model, HttpSession session){

        List<Producto> productos = productoService.findAll();

        model.addAttribute("productos", productos);

        //session

        model.addAttribute("sesion", session.getAttribute("idUsuario"));


        //obtener el nombre de usuario
        String username= (String) session.getAttribute("username");
        System.out.println(username);
        model.addAttribute("username", username);
        return "usuario/home";
    }

@GetMapping("producto/{id}")
    public String productoHome(@PathVariable Integer id, Model model){

        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);
        return "usuario/singleproduct";
    }

    @PostMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model){
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

      Optional<Producto> optionalProducto = productoService.get(id);

      producto = optionalProducto.get();

      detalleOrden.setCantidad(cantidad);
      detalleOrden.setPrecio(producto.getPrecio());
      detalleOrden.setNombre(producto.getNombre());
      detalleOrden.setTotal(producto.getPrecio()*cantidad);
      detalleOrden.setProducto(producto);

      // validacion que un mismo producto si es agregado, no se añada 2 veces, solo se sumen unidades

        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p-> p.getProducto().getId()==idProducto);

        if(!ingresado){
            detalles.add(detalleOrden);

        }


      sumaTotal= detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
      orden.setTotal(sumaTotal);

      model.addAttribute("cart", detalles);
      model.addAttribute("orden", orden);


        return "usuario/cart";
    }

    @GetMapping("/delete/cart/{id}")
    public String deleteProductCart(@PathVariable Integer id, Model model){
        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
        for(DetalleOrden detalleOrden: detalles){
            if(detalleOrden.getProducto().getId()!=id){
                ordenesNuevas.add(detalleOrden);
            }
        }
        detalles=ordenesNuevas;

        double sumaTotal=0;

        sumaTotal= detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
        orden.setTotal(sumaTotal);

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return"usuario/cart";
    }
@PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getcart")
    public String getCart(Model model, HttpSession session){
        model.addAttribute("cart", detalles);//son variables globales
        model.addAttribute("orden", orden); //tambien

        //session
        model.addAttribute("sesion", session.getAttribute("idUsuario"));
        return"/usuario/cart";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session){

        Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();


        model.addAttribute("cart", detalles);//son variables globales
        model.addAttribute("orden", orden); //tambien
        model.addAttribute("usuario", user);
        return "/usuario/resumenorden";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session){
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumOrden());

        //usuario
        Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();

        orden.setUsuario(user);
        ordenService.save(orden);

        //guardar detalles
        for(DetalleOrden dt:detalles){
            dt.setOrden(orden);
            detalleOrdenService.save(dt);
        }
        //limpiar valores para que pueda seguir comprando si quiere

        orden = new Orden();
        detalles.clear();
        return "redirect:/";
    }


    @PostMapping("/search")
    public String searchProduct(@RequestParam String busqueda, Model model){
        List<Producto> productos = productoService.findAll().stream().filter(p->p.getNombre().toLowerCase().contains(busqueda.toLowerCase())).collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }
}
