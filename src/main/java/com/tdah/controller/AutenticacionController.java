package com.tdah.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.model.Usuario;
import com.tdah.service.IUsuarioService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/autenticacion")
@Slf4j
public class AutenticacionController {
	
	@Autowired
	IUsuarioService usuarioService;
	
	@GetMapping("/login")
	public String vistaLogin(@RequestParam(value = "error", required = false) String error, Model model) {
		log.info("login");
		if(error!=null) {
			model.addAttribute("mensaje_error","Usuario y/o clave incorrectos");
		}	
		Usuario usuario = new Usuario();
		model.addAttribute("usuario", usuario);
		
		return "autenticacion/login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Valid Usuario usuario, BindingResult result, RedirectAttributes flash) {
		log.info(""+usuario);
		
		Usuario usuarioRegistrado = new Usuario();
		try {
			usuarioRegistrado = usuarioService.usuarioLogin(usuario.getNombreUsuario(), usuario.getClave());
		} catch (Exception e) {
			log.error(""+ e.getMessage());
		}
		
		if(usuarioRegistrado != null) {
			return "redirect:/encuesta/registrar";
		} else {
			flash.addFlashAttribute("mensaje_error","Credenciales incorrectas, vuelva a intentarlo.");
			return "redirect:/autenticacion/login";
		}
		
		
	}

}
