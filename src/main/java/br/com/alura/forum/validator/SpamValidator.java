package br.com.alura.forum.validator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;

@Component
public class SpamValidator implements Validator {

	@Autowired
	private TopicRepository topicRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return NewTopicInputDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
		List<Topic> topics = topicRepository.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggedUser,
				oneHourAgo);

		if (topics.size() >= 4) {
			Instant instantOfTheOldestTopic = topics.get(0).getCreationInstant();

			long minutesToNextTopic = Duration.between(oneHourAgo, instantOfTheOldestTopic).getSeconds() / 60;

			errors.reject("newTopicInputDto.limit.exceeded", new Object[] { minutesToNextTopic },
					"O limite individual de novos tópicos por hora foi excedido");

		}
	}

}
