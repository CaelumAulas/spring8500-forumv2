package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicDashboardItemOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.controller.dto.output.ValidationErrorsOutputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.DashboardDataProcessingService;
import br.com.alura.forum.vo.CategoriesAndTheirStatisticsData;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private DashboardDataProcessingService dashboardDataProcessingService;
	@Autowired
	private CourseRepository courseRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch,
            @PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {

        Specification<Topic> topicSearchSpecification = topicSearch.build();
        Page<Topic> topics = this.topicRepository.findAll(topicSearchSpecification, pageRequest);

        return TopicBriefOutputDto.listFromTopics(topics);
    }
 
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TopicDashboardItemOutputDto> getDashboardInfo() {
    
    		CategoriesAndTheirStatisticsData categoriesStatisticsData = this.dashboardDataProcessingService.execute();
    		return TopicDashboardItemOutputDto.listFromCategories(categoriesStatisticsData);
    		
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    			produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> salva(
    		@Valid @RequestBody NewTopicInputDto inputDto,
    		@AuthenticationPrincipal User loggedUser) {
		Topic topic = inputDto.build(courseRepository, loggedUser);
		topicRepository.save(topic);
		
		URI location = UriComponentsBuilder.fromPath("/api/topics/{id}")
				.buildAndExpand(topic.getId()).toUri();
		TopicOutputDto topicOutputDto = new TopicOutputDto(topic);
		return ResponseEntity.created(location).body(topicOutputDto);
    }
    

    
    
    
    
}
