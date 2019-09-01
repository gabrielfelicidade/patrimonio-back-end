package br.edu.fatecsorocaba.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.edu.fatecsorocaba.system.model.Location;
import br.edu.fatecsorocaba.system.model.Patrimony;
import br.edu.fatecsorocaba.system.repository.LocationRepository;

@Component
public class LocationService {
	
	public List<Map<String, Object>> getLocationsPatrimoniesReportData(LocationRepository repository){
		List<Map<String, Object>> map = new ArrayList<>();
		for(Location location : repository.findAll()) {
			for(Patrimony patrimony : location.getPatrimonies()) {
				Map<String, Object> item = new HashMap<>();
				item.put("locationDescription", location.getDescription());
				item.put("patrimonyId", patrimony.getPatrimonyId());
				item.put("patrimonyDescription", patrimony.getDescription());
				item.put("patrimonyModel", patrimony.getModel());
				item.put("patrimonyBrand", patrimony.getBrand());
				item.put("patrimonyValue", patrimony.getValue());
				map.add(item);
			}
		}
		return map;
	}
	
	public List<Map<String, Object>> getLocationsPatrimoniesReportData(LocationRepository repository, long id){
		List<Map<String, Object>> map = new ArrayList<>();
		Location location = repository.findById(id).orElse(null);
		if (location != null) {
			for(Patrimony patrimony : location.getPatrimonies()) {
				Map<String, Object> item = new HashMap<>();
				item.put("locationDescription", location.getDescription());
				item.put("patrimonyId", patrimony.getPatrimonyId());
				item.put("patrimonyDescription", patrimony.getDescription());
				item.put("patrimonyModel", patrimony.getModel());
				item.put("patrimonyBrand", patrimony.getBrand());
				item.put("patrimonyValue", patrimony.getValue());
				map.add(item);
			}
		}
		return map;
	}
}
