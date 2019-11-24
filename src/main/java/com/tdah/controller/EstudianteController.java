package com.tdah.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tdah.dto.EstudianteDTO;
import com.tdah.model.Contacto;
import com.tdah.model.Estudiante;
import com.tdah.service.IEstudianteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/estudiante")
@Slf4j
public class EstudianteController {
	@Autowired
	IEstudianteService estudianteService;
	
	@GetMapping("/listar")
	public ModelAndView listarEstudiantes() {
		ModelAndView model = new ModelAndView("estudiante/listar-estudiante");
		log.info("list estudiantes");
		List<EstudianteDTO> estudiantes = estudianteService.listarEstudiantesFront();
		
		model.addObject("estudiantes", estudiantes);
		return model;
	}
	
	
	@GetMapping("/registrar")
	public String getRegistrarEstudiante(Map<String, Object> model) {
		Estudiante estudiante = new Estudiante();
		
		List<Contacto> contactos = new ArrayList<Contacto>();
		
		Contacto contacto = new Contacto();
		contacto.setDireccion("");
		contacto.setCorreoElectronico("");
		contacto.setNumeroTelefonico("");
		
		contactos.add(contacto);
		
		estudiante.setContactos(contactos);
		
		model.put("estudiante", estudiante);
		model.put("contactos", contactos);
		return "estudiante/registrar-estudiante";
	}
	
	@PostMapping("/registrar")
	public String postRegistrarEstudiante(@Valid Estudiante estudiante, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

		try {
			estudianteService.saveOrUpdate(estudiante);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editarEstudiante(@PathVariable(value = "id") Integer id, Map<String, Object> model, RedirectAttributes flash) {

		Estudiante estudiante = estudianteService.findById(id);
		
		if (estudiante == null) {
			flash.addFlashAttribute("error", "El estudiante no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("estudiante", estudiante);
		return "estudiante/editar-estudiante";
	}

}
