package com.adcaisse.compta.EndPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcaisse.compta.repository.JournalRepository;
import com.bprice.persistance.model.Journall;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class JournalController {
	
	  @Autowired
		private JournalRepository journallRepository;

		@GetMapping("/journall")
		public List<Journall> getAllJournall() {
			return journallRepository.findAll();
		}

		@GetMapping("/journall/{id}")
		public ResponseEntity<Journall> getJournallById(@PathVariable(value = "id") String journallId)
				throws ResourceNotFoundException {
			Journall journall = journallRepository.findById(journallId)
					.orElseThrow(() -> new ResourceNotFoundException("Journall not found for this id :: " + journallId));
			return ResponseEntity.ok().body(journall);
		}

		@PostMapping("/journall")
		public Journall createjournall(@Valid @RequestBody Journall journall) {
			return journallRepository.save(journall);
		}

		@PutMapping("/journall/{id}")
		public ResponseEntity<Journall> updateJournall(@PathVariable(value = "id") String journallId,
				@Valid @RequestBody Journall JournallDetails) throws ResourceNotFoundException {
			Journall journall = journallRepository.findById(journallId)
					.orElseThrow(() -> new ResourceNotFoundException("Journall not found for this id :: " + journallId));

			journall.setId(JournallDetails.getId());
			journall.setIsActif(JournallDetails.getIsActif());
			journall.setTypeJournal(JournallDetails.getTypeJournal());
			final Journall updateJournall = journallRepository.save(journall);
			return ResponseEntity.ok(updateJournall);
		}

		@DeleteMapping("/journall/{id}")
		public Map<String, Boolean> deleteJournall(@PathVariable(value = "id") String journallId)
				throws ResourceNotFoundException {
			Journall journall = journallRepository.findById(journallId)
					.orElseThrow(() -> new ResourceNotFoundException("Journall not found for this id :: " + journallId));

			journallRepository.delete(journall);
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.TRUE);
			return response;
		}
}
