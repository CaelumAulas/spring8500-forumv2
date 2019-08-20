package br.com.alura.forum.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.alura.forum.model.Category;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class TopicRepositoryTest {

	@Autowired
	private TopicRepository topicRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Test
	public void shouldReturnAllTopicsOfProgramming() throws Exception {
	
		Category category = categoryRepo.findByName("Programação");
		int openTopics = topicRepo.countTopicsByCategory(category);
		
		assertThat(openTopics).isGreaterThan(0);
		assertThat(openTopics).isEqualTo(9);
	}
	
	@Test
	public void shouldReturnLastWeekTopicsOfProgramming() throws Exception {
	
		Category category = categoryRepo.findByName("Programação");
		
		Instant lastWeek = Instant.now().minus(7, ChronoUnit.DAYS);
		int lastWeekTopics = this.topicRepo.countLastWeekTopicsByCategory(category, lastWeek);

		assertThat(lastWeekTopics).isEqualTo(1);
	}
	
	@Test
	public void shouldReturnUnansweredTopicsOfProgramming() throws Exception {
	
		Category category = categoryRepo.findByName("Programação");
		
		int unansweredTopics = this.topicRepo.countUnansweredTopicsByCategory(category);

		assertThat(unansweredTopics).isEqualTo(6);
	}
	
	
}
