package com.promotion.handwriting.scenario;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.WebConfig;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.SearchContentsRequest;
import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.repository.database.UserRepository;
import com.promotion.handwriting.security.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.promotion.handwriting.enums.AdType.CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ScenarioOne extends TestClass {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder encoder;

    String token;

    @BeforeEach
    void initAdminToken() {
        User adminUser = userRepository.saveAndFlush(User.builder()
                .userId("test")
                .password(encoder.encode("1234"))
                .type(UserType.ADMIN).build());
        token = "Bearer " + jwtService.createJwt(adminUser.getUserId());
    }

    @AfterEach
    void delete(){
        userRepository.deleteAllInBatch();
    }

    @DisplayName("컨텐츠를 생성하고 조회하면 컨텐츠의 내용이 저장되어야 한다.")
    @Test
    void whenCreateContentAndFindThenMustExistContentExactly() throws Exception {
        // given

        var createContent = new CreateContentRequest();
        createContent.setTitle("new content");
        createContent.setDescription("new detail");
        var dto = new MockMultipartFile("dto", "dto", "application/json", mapper.writeValueAsString(createContent).getBytes());
        // when
        mockMvc.perform(multipart(HttpMethod.PUT, "/data/content")
                        .file(dto)
                        .header("Authorization", token)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success")
                );
        // then
        var searchRequest = new SearchContentsRequest();
        searchRequest.setType(CONTENT);
        mockMvc.perform(get("/data/content")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(searchRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data.content[?(@.title=='new content')]").exists()
                );
    }

    @Nested
    class WhenSaveImageThenCanAccessImageUrl {

        @Value("#{systemProperties['user.dir']}")
        private String projectRootPath;
        @Value("${directory.image}")
        private String imageDir;
        @Autowired
        WebConfig config;

        @BeforeEach
        @AfterEach
        void beforeAndAfter() {
            File dir = new File(config.getImageDirectory());
            for (File f : dir.listFiles()) {
                System.out.println(f.delete());
            }
        }

        @DisplayName("새로운 컨텐츠 생성시 이미지 포함한다면 반환된 이미지 리소스 url 조회시 200 OK 응답을 받아야한다.")
        @Test
        void whenSaveImageThenCanAccessImageUrl() throws Exception {
            // given
            var createContent = new CreateContentRequest();
            createContent.setTitle("new content");
            createContent.setDescription("new detail");
            var sample = new File(projectRootPath + "/src/main/resources/static/image/no_image.jpg");
            var dto = new MockMultipartFile("dto", "dto", "application/json", mapper.writeValueAsString(createContent).getBytes());
            var image = new MockMultipartFile("image", "new.jpg", MediaType.IMAGE_JPEG_VALUE, Files.readAllBytes(sample.toPath()));
            // when
            MvcResult result = mockMvc.perform(multipart(HttpMethod.PUT, "/data/content")
                            .file(dto)
                            .file(image)
                            .header("Authorization", token)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value("success"),
                            jsonPath("$.data.images[0].original").exists()
                    ).andReturn();

            String imageUrl = config.getImageUrl();
            Matcher originalUrlMatcher = Pattern.compile("\"original\"\s*:\s*\"" + imageUrl + "([^\"]*)\"").matcher(result.getResponse().getContentAsString());
            Matcher compressurlMatcher = Pattern.compile("\"compress\"\s*:\s*\"" + imageUrl + "([^\"]*)\"").matcher(result.getResponse().getContentAsString());

            assertThat(originalUrlMatcher.find()).isTrue();
            assertThat(compressurlMatcher.find()).isTrue();

            String originalUrl = originalUrlMatcher.group(1);
            String compressUrl = originalUrlMatcher.group(1);

            File origin = new File(projectRootPath + imageDir + originalUrl);
            File compress = new File(projectRootPath + imageDir + compressUrl);

            // then
            assertThat(origin.exists()).isTrue();
            assertThat(compress.exists()).isTrue();

            mockMvc.perform(get(imageUrl + originalUrl)).andExpect(status().isOk());
            mockMvc.perform(get(imageUrl + compressUrl)).andExpect(status().isOk());
        }

    }

}
