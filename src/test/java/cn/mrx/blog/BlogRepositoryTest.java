package cn.mrx.blog;

import static org.assertj.core.api.Assertions.assertThat;

import cn.mrx.blog.model.User;
import cn.mrx.blog.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogRepositoryTest {

	/**
	@Autowired
	private EsBlogRepository esBlogRepository;

	@Test
	public void test01() {
		esBlogRepository.deleteAll();
		esBlogRepository.save(new EsBlog("登鹳雀楼", "王之涣的登鹳雀楼", "白日依山尽，黄河入海流。欲穷千里目，更上一层楼。"));
		esBlogRepository.save(new EsBlog("相思", "王维的相思", "红豆生南国， 春来发几枝。愿君多采撷， 此物最相思。"));
		esBlogRepository.save(new EsBlog("静夜思", "李白的静夜思", "床前明月光， 疑是地上霜。举头望明月， 低头思故乡。"));

		String title = "思";
		String summary = "相思";
		String content = "相思";
		Pageable pageable = new PageRequest(0, 20);
		Page<EsBlog> esBlogPage = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content, pageable);
		for (EsBlog esBlog : esBlogPage){
			System.out.println(esBlog);
		}
		assertThat(esBlogPage.getTotalElements()).isEqualTo(2);
	}*/


	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	public void test01(){
//		Pageable pageable = new PageRequest(0, 10);
//		User user = userS
//		blogRepository.findByUserAndTitleLike("", );

		User user = userService.getUserById(1L);
		System.out.println(user);
	}

}
