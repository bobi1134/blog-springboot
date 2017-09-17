package cn.mrx.blog;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlogControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test01() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user/load").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.get("/user/load").accept(MediaType.APPLICATION_JSON)).andExpect(content().string(equalTo("abc")));
	}

}
