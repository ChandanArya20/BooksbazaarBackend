package in.ineuron.restcontrollers;

import java.util.Optional;

import in.ineuron.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import in.ineuron.models.ImageFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/image")
public class ImageFileController {

	private BookService bookService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBookImageById(@PathVariable Long id){
		
		Optional<ImageFile> imageFileOptional = bookService.fetchBookImageById(id);
		
		if(imageFileOptional.isPresent()) {
			
			ImageFile imageFile = imageFileOptional.get();
			
			return ResponseEntity.ok()
					.contentType(MediaType.valueOf(imageFile.getType()))
					.body(imageFile.getImageData());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found for this image id");
		}		
	}
	

}



	