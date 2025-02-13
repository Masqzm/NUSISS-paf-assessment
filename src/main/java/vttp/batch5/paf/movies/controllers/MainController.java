package vttp.batch5.paf.movies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch5.paf.movies.services.MovieService;

@RestController
@RequestMapping(path = "/api")
public class MainController {
  @Autowired
  private MovieService movieSvc;

  // TODO: Task 3
  @GetMapping("/summary")
	@ResponseBody
	public ResponseEntity<String> getProlificDirectors(@RequestParam int count) {
    movieSvc.getProlificDirectors(count);
    
		// return JSON array
    return ResponseEntity.ok("{}");
	}

  
  // TODO: Task 4


}
