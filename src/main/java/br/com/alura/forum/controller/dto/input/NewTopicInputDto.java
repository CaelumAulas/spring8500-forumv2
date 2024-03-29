package br.com.alura.forum.controller.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;

public class NewTopicInputDto {
	@Size(min = 5, message = "Preencha a descrição")
	private String shortDescription;
	@NotBlank
	@Size(min = 5)
    private String content;
    private String courseName;
    
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Topic build(CourseRepository courseRepository, User loggedUser) {
		Course course = courseRepository.findByName(courseName);
		return new Topic(shortDescription, content, loggedUser, course); 
	}


}
