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
	
	public List<Map<String, Object>> getLocationsPatrimoniesReport(LocationRepository repository){
		List<Map<String, Object>> map = new ArrayList<>();
		for(Location location : repository.findAll()) {
			for(Patrimony patrimony : location.getPatrimonies()) {
				Map<String, Object> item = new HashMap<>();
				item.put("locationDescription", location.getDescription());
				item.put("patrimonyId", patrimony.getPatrimonyId());
				item.put("patrimonyDescription", patrimony.getDescription());
				map.add(item);
			}
		}
		return map;
	}

}
