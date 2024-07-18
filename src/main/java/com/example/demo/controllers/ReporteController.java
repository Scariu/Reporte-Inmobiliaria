package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Reporte;
import com.example.demo.services.ReporteService;

@RestController
@CrossOrigin()
public class ReporteController {
	@Autowired
	private ReporteService reporteService;

	@GetMapping("/reportes")
	public List<Reporte> getAllReportes() {
		return reporteService.getAllReportes();
	}

	@GetMapping(value = "/reportes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Reporte> getReporteById(@PathVariable Long id) {
		Reporte reportes = reporteService.getReporteById(id);
		if (reportes == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reportes);
	}

	@PostMapping(value = "/reportes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Reporte createReporte(@RequestBody Reporte reporte) {
		return reporteService.saveReporte(reporte);
	}

	@PutMapping("/reporte/{id}")
	public ResponseEntity<Reporte> updateReporte(@PathVariable Long id, @RequestBody Reporte reportes) {
		Reporte reporte = reporteService.getReporteById(id);
		if (reporte == null) {
			return ResponseEntity.notFound().build();
		}
		reporte.setNombre(reportes.getNombre());
		reporte.setDescripcion(reportes.getDescripcion());
		reporte.setPrecio(reportes.getPrecio());
		Reporte actualizarReporte = reporteService.saveReporte(reporte);
		return ResponseEntity.ok(actualizarReporte);
	}

	@DeleteMapping("/reportes/{id}")
	public ResponseEntity<Void> deleteReporte(@PathVariable Long id) {
		Reporte reporte = reporteService.getReporteById(id);
		if (reporte == null) {
			return ResponseEntity.notFound().build();
		}
		reporteService.deleteReporte(id);
		return ResponseEntity.noContent().build();
	}
}
