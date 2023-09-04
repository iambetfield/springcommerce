package com.iternova.demo.controller;

import com.iternova.demo.model.Producto;
import com.iternova.demo.model.Usuario;
import com.iternova.demo.service.ProductoService;
import com.iternova.demo.service.UploadFileService;
import com.iternova.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UploadFileService upload;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "productos/show.html";
    }


    @GetMapping("/create")
    public String create(){
        return  "productos/create.html";
    }

    @PostMapping("/save")                               //id="img" de la vista de create.html
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {



       Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();

       producto.setUsuario(user);

       //imagen
        if(producto.getId()==null){ //cuando se crea un producto -el id es null aun-
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

       productoService.save(producto);
        return "redirect:/productos";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Producto producto = new Producto();
        Optional<Producto> OptionalProducto = productoService.get(id);
        producto = OptionalProducto.get();

        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException{
        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();

        if(file.isEmpty()){ //editamos el producto pero no cambiamos la imagen

            producto.setImagen(p.getImagen());
        }else { //cuando se edita la imagen



            //eliminar cuando no es la imagen por defecto
            if(p.getImagen().equals("default.png")){
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);

        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Producto p = new Producto();
        p = productoService.get(id).get();
        //eliminar cuando no es la imagen por defecto
        if(p.getImagen().equals("default.png")){
            upload.deleteImage(p.getImagen());
        }
        productoService.delete(id);
        return "redirect:/productos";
    }


}
