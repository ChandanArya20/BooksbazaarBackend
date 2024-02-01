package in.ineuron.models;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class ImageFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String type;
	
	@Lob
	@Column(nullable = false)
	private byte[] imageData;
}
