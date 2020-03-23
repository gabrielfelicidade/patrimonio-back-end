package br.edu.fatecsorocaba.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.edu.fatecsorocaba.system.model.Patrimony;
import br.edu.fatecsorocaba.system.repository.PatrimonyRepository;

@Component
public class PatrimonyService {

	public List<Map<String, Object>> getPatrimoniesWritedOffByDateReportData(PatrimonyRepository repository, int year, int month){
		List<Map<String, Object>> map = new ArrayList<>();
		for(Patrimony patrimony : repository.findByYearAndMonth(year, month)) {
			Map<String, Object> item = new HashMap<>();
			item.put("patrimonyId", patrimony.getPatrimonyId());
			item.put("patrimonyDescription", patrimony.getDescription());
			item.put("patrimonyModel", patrimony.getModel());
			item.put("patrimonyBrand", patrimony.getBrand());
			item.put("patrimonyValue", patrimony.getValue());
			map.add(item);
		}
		return map;
	}
	
}
