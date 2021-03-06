package com.searcin.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.searcin.entity.Areas;
import com.searcin.entity.Categories;
import com.searcin.entity.Services;
import com.searcin.entity.SubCategories;
import com.searcin.entity.Vendors;
import com.searcin.service.AreasService;
import com.searcin.service.CategoriesService;
import com.searcin.service.IngestionService;
import com.searcin.service.ServicesService;
import com.searcin.service.SubCategoriesService;
import com.searcin.service.VendorsService;
import com.searcin.utils.ESMapper;

@RestController
@RequestMapping("/ingest")
public class IngestionController {	
	
	@Autowired
	private ESMapper esMapper;
	
	@Autowired
	private IngestionService ingestionService;
	
	@Autowired
	private AreasService areasService;

	@Autowired
	private CategoriesService categoriesService;

	@Autowired
	private ServicesService servicesService;

	@Autowired
	private SubCategoriesService subCategoriesService;

	@Autowired
	private VendorsService vendorsService;
	
	@RequestMapping(value = "/reset")
	public void rest() {		
		ingestionService.reset();		
	}
	
	@RequestMapping(value = "/sync")
	public void push() {
		ingestionService.reset();	
		List<Services> services = servicesService.findAll();		
		if(services != null && services.size() > 0) {
			ingestionService.service(services.stream()
					.map(item -> esMapper.toES(item)).collect(Collectors.toList()));
		}		
		
		List<Areas> areas = areasService.findAll();		
		if(areas != null && areas.size() > 0) {
			ingestionService.area(areas.stream()
					.map(item -> esMapper.toES(item)).collect(Collectors.toList()));			
		}
		
		List<SubCategories> subCategories = subCategoriesService.findAll();
		if(subCategories != null && subCategories.size() > 0) {
			ingestionService.subcategory(subCategories.stream()
					.map(item -> esMapper.toES(item)).collect(Collectors.toList()));
		}		
		
		List<Categories> categories = categoriesService.findAll();
		if(categories != null && categories.size() > 0) {
			ingestionService.category(categories.stream()
					.map(item -> esMapper.toES(item)).collect(Collectors.toList()));
		}			
		
		List<Vendors> vendors = vendorsService.findAll();		
		if(vendors != null && vendors.size() > 0) {
			ingestionService.vendor(vendors.stream()
					.map(item -> esMapper.toES(item)).collect(Collectors.toList()));
		}
	}
}
