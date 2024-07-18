package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Reporte;
import com.example.demo.repositories.ReporteRepository;

@Service
public class ReporteService {
	@Autowired
	private ReporteRepository reporteRepository;

	public List<Reporte> getAllReportes() {
		return reporteRepository.findAll();
	}

	public Reporte getReporteById(Long id) {
		return reporteRepository.findById(id).orElse(null);
	}

	public Reporte saveReporte(Reporte reporte) {
		return reporteRepository.save(reporte);
	}

	public void deleteReporte(Long id) {
		reporteRepository.deleteById(id);
	}

}
