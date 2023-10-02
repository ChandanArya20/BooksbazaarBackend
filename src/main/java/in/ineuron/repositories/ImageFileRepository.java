package in.ineuron.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ineuron.models.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long>{
	
	public Optional<ImageFile> findById(Long id);

}
