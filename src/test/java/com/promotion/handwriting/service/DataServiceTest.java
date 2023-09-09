package com.promotion.handwriting.service;

import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.WebConfig;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.request.ChangeMainPageRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.SearchContentsRequest;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.JpaImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import com.promotion.handwriting.service.business.DataService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.promotion.handwriting.enums.AdType.CONTENT;
import static com.promotion.handwriting.enums.AdType.INTRO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class DataServiceTest extends TestClass {

    @Autowired
    AdRepository adRepository;
    @Autowired
    JpaImageRepository imageRepository;
    @Autowired
    DataService dataService;
    @Autowired
    WebConfig config;
    @MockBean
    FileRepository fileRepository;

    @AfterEach
    public void after() {
        imageRepository.deleteAllInBatch();
        adRepository.deleteAllInBatch();
    }

    @DisplayName("표지 화면에 필요한 정보를 반환한다.")
    @Test
    public void mainPageData() {
        // given
        var content = saveContent(INTRO, "this is title", "hello world");
        saveImage(content, "test.jpg");
        // when
        MainPageDto mainPageData = dataService.mainPageData();
        // then
        assertThat(mainPageData)
                .extracting("title", "description")
                .containsExactly("this is title", "hello world");
        assertThat(mainPageData.getImageUrl()).matches(config.getImageUrl() + "/.*test.jpg");
    }

    @DisplayName("새로운 컨텐츠 추가")
    @Nested
    class newContent {

        CreateContentRequest request;

        @BeforeEach
        public void init() {
            request = new CreateContentRequest();
            request.setDescription("hello content");
            request.setTitle("this is content");
        }

        @DisplayName("이미지 정보가 없을 때")
        @Test
        public void whenNoImage() {
            //given //when
            ContentDto dto = dataService.newContent(request, null);
            Ad content = adRepository.findById(Long.parseLong(dto.getId()));
            //then
            assertThat(content).extracting("title", "detail")
                    .containsExactly("this is content", "hello content");
        }

        @DisplayName("이미지 정보가 있을 때")
        @Test
        public void whenImageExist() {
            //given
            MultipartFile image1 = Mockito.mock(MultipartFile.class);
            MultipartFile image2 = Mockito.mock(MultipartFile.class);
            when(image1.getOriginalFilename()).thenReturn("image.jpg");
            when(image2.getOriginalFilename()).thenReturn("image.jpg");
            List<MultipartFile> images = List.of(image1, image2);
            //when
            ContentDto dto = dataService.newContent(request, images);
            Ad content = adRepository.findById(Long.parseLong(dto.getId()));
            //then
            assertThat(content).extracting("title", "detail")
                    .containsExactly("this is content", "hello content");
            dto.getImages().forEach(url -> {
                assertThat(url.getOriginal()).matches(config.getImageUrl() + "/.*image.jpg");
                assertThat(url.getCompress()).matches(config.getImageUrl() + "/zip-.*image.jpg");
            });
        }
    }

    @DisplayName("이미지 추가시 저장이 되어야한다.")
    @Test
    public void newImage() {
        //given
        var content = saveContent(CONTENT, "title", "detail");
        var id = content.getId();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image1.jpg");
        //when
        String imageName = dataService.newImage(file, id);
        //then
        assertThat(imageName).matches("/.*" + file.getOriginalFilename());
    }

    @DisplayName("컨텐츠 내용 조회시")
    @Nested
    class GetContent {

        @BeforeEach
        void init() {
            for (int i = 0; i < 20; i++) {
                saveContent(CONTENT, "content" + i, "detail" + i);
            }
        }

        @DisplayName("페이지 사이즈 5이고 페이지 인덱스 0일때")
        @Test
        void whenPageSize5AndPageNumber0() {
            //given
            Pageable pageable = PageRequest.of(0, 5);
            var request = new SearchContentsRequest();
            request.setType(CONTENT);
            //when
            Page<ContentDto> result = dataService.getContentDtos(request, pageable);
            //then
            assertThat(result.getNumberOfElements()).isEqualTo(5);
            assertThat(result.getNumber()).isZero();
        }

        @DisplayName("INTRO 타입이 존재하지 않을 때")
        @Test
        void whenGetIntroContent() {
            //given
            Pageable pageable = PageRequest.of(0, 5);
            var request = new SearchContentsRequest();
            request.setType(INTRO);
            //when
            Page<ContentDto> result = dataService.getContentDtos(request, pageable);
            //then
            assertThat(result).hasSize(0);
        }
    }

    @DisplayName("이미지 데이터 조회시")
    @Nested
    class GetImage {

        String contentId;

        @BeforeEach()
        void init() {
            Ad content = saveContent(CONTENT, "content", "detail");
            for (int i = 0; i < 20; i++) {
                saveImage(content, "fileGetImage" + i + ".jpg");
            }
            contentId = content.getId() + "";
        }

        @ParameterizedTest
        @DisplayName("페이지 사이즈와 페이지 인덱스가 xx 일때")
        @CsvSource(useHeadersInDisplayName = true, textBlock = """
                page size, page index, expected count
                5, 0, 5
                30, 0, 20
                5, 2, 5
                """)
        public void whenPageSizeAndPageIndex(int size, int index, int expectedCount) {
            //given
            Pageable pageable = PageRequest.of(index, size);
            //when
            var result = dataService.getImageUrls(contentId, pageable);
            //then
            assertThat(result.getNumberOfElements()).isEqualTo(expectedCount);
            assertThat(result.getNumber()).isEqualTo(index);
        }
    }


    @DisplayName("컨텐츠를 수정")
    @Test
    public void updateContent() {
        //given
        long id = saveContent(CONTENT, "before", "before detail").getId();
        var request = new ChangeContentRequest();
        request.setId(id);
        request.setDescription("after detail");
        request.setTitle("after title");
        // when
        dataService.updateContent(request);
        Ad afterDto = adRepository.findById(id);
        // then
        assertThat(afterDto).extracting("title", "detail", "id")
                .containsExactly("after title", "after detail", id);
    }

    @Nested
    class MainPageUpdate{
        @DisplayName("메인 페이지 수정")
        @Test
        public void updateMainPage() {
            // before
            saveContent(INTRO, "main page", "detail");
            //given
            for(int time = 0; time < 3; time++) {
                var file = mock(MultipartFile.class);
                String imageFileName = time + "mock.jpg";
                when(file.getOriginalFilename()).thenReturn(imageFileName);

                var request = new ChangeMainPageRequest();
                request.setDescription("after detail" + time);
                request.setTitle("after title" + time);
                //when
                dataService.updateMainPage(request, file);
                //then
                MainPageDto afterMainPage = dataService.mainPageData();
                assertThat(afterMainPage).extracting("title", "description")
                        .containsExactly("after title" + time, "after detail" + time);
                assertThat(afterMainPage.getImageUrl()).matches(config.getImageUrl() + "/.*" + imageFileName);
            }
        }
    }

    @DisplayName("이미지 삭제")
    @Test
    public void deleteImages() {
        //given
        Ad content = saveContent(CONTENT, "c", "d");
        Image image = saveImage(content, "image.jpg");
        //when
        dataService.deleteImages(List.of(image.getId()), content.getId());
        //then
        assertThat(imageRepository.findById(image.getId())).isEmpty();
    }

    @DisplayName("컨텐츠 삭제")
    @Test
    public void deleteContent() {
        //given
        Ad content = saveContent(CONTENT, "c", "d");
        saveImage(content, "image.jpg");
        //when
        dataService.deleteContent(content.getId());
        //then
        assertThat(adRepository.findById(content.getId())).isNull();
        assertThat(imageRepository.findByAdId(content.getId(), PageRequest.of(0, 1000))).hasSize(0);
    }

    private Image saveImage(Ad content, String originalName) {
        return imageRepository.saveAndFlush(Image.builder()
                .imageName(originalName)
                .content(content)
                .build());
    }

    private Ad saveContent(AdType adType, String title, String detail) {
        return adRepository.saveAndFlush(Ad.builder()
                .type(adType)
                .title(title)
                .detail(detail)
                .build());
    }
}