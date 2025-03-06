package com.telcel.JiraExportWeb.controller;

import com.telcel.JiraExportWeb.service.JiraExportFileService;
import com.telcel.JiraExportWeb.service.JiraService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jira")
@CrossOrigin(origins = "*")  // Permitir peticiones desde el frontend
public class JiraRestController {

    private final JiraService jiraService;
    private final JiraExportFileService jiraExportFileService;

    public JiraRestController(JiraService jiraService) {
        this.jiraService = jiraService;
		this.jiraExportFileService = new JiraExportFileService();
    }
      

    @GetMapping("/consultar")
    public List<Map<String, String>> obtenerDatosJira(@RequestParam String fechaInicio, @RequestParam String fechaFinal) {
        return jiraService.consultarJira(fechaInicio,fechaFinal);
    }
    
    @GetMapping("/descargaCSV")
    public void descargaDocumento(@RequestParam String path) {
    	jiraExportFileService.descargaDocumento(path);
    }
}