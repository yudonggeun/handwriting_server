package com.promotion.handwriting.service;

import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import com.promotion.handwriting.service.business.DataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.promotion.handwriting.enums.AdType.CONTENT;
import static com.promotion.handwriting.enums.AdType.INTRO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
class DataServiceTest extends TestClass {

    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    DataService dataService;
    @MockBean
    FileRepository fileRepository;


    @Value("${spring.url.image}")
    private String imageUrl;

    @AfterEach
    public void after() {
        imageRepository.deleteAllInBatch();
        adRepository.deleteAllInBatch();
    }

    @DisplayName("표지 화면에 필요한 정보를 반환한다.")
    @Test
    public void mainPageData() {
        // given
        Ad content = createContent("this is title", INTRO, "hello world");
        content.getImages().add(createImage(content, "test.jpg", "compress.jpg"));
        adRepository.save(content);
        new MainPageDto("this is title", "/test.jpg", "hello world");
        // when
        MainPageDto mainPageData = dataService.mainPageData();
        // then
        assertThat(mainPageData)
                .extracting("title", "imageUrl", "description")
                .containsExactly("this is title", imageUrl + content.getResourcePath() + "/test.jpg", "hello world");
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
            //then
            assertThat(dto).extracting("title", "description")
                    .containsExactly("this is content", "hello content");
        }

        @DisplayName("이미지 정보가 있을 때")
        @Test
        public void whenImageExist() {
            //given
            MultipartFile image1 = Mockito.mock(MultipartFile.class);
            MultipartFile image2 = Mockito.mock(MultipartFile.class);
            when(image1.getOriginalFilename()).thenReturn("image1.jpg");
            when(image2.getOriginalFilename()).thenReturn("image2.jpg");
            List<MultipartFile> images = List.of(image1, image2);
            //when
            ContentDto dto = dataService.newContent(request, images);
            //then
            assertThat(dto).extracting("title", "description")
                    .containsExactly("this is content", "hello content");
            assertThat(dto.getImages()).map(urls -> urls.getOriginal())
                    .containsExactly(imageUrl + request.getPath() + "/image1.jpg",
                            imageUrl + request.getPath() + "/image2.jpg");
        }
    }

    @DisplayName("이미지 추가시 저장이 되어야한다.")
    @Test
    public void newImage() {
        //given
        var content = adRepository.saveAndFlush(createContent("title", CONTENT, "detail"));
        var id = content.getId();
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image1.jpg");
        //when
        String imageName = dataService.newImage(file, id);
        //then
        assertThat(imageName).isEqualTo(file.getOriginalFilename());
    }

    @DisplayName("컨텐츠 내용 조회시")
    @Nested
    class GetContent {

        @BeforeEach
        void init() {
            for (int i = 0; i < 20; i++) {
                saveContent(CONTENT, "content" + i, "detail" + i, "/root");
            }
        }

        @DisplayName("페이지 사이즈 5이고 페이지 인덱스 0일때")
        @Test
        void whenPageSize5AndPageNumber0() {
            //given
            Pageable pageable = PageRequest.of(0, 5);
            //when
            Page<ContentDto> result = dataService.getContentDtos(CONTENT, pageable);
            //then
            assertThat(result.getNumberOfElements()).isEqualTo(5);
            assertThat(result.getNumber()).isZero();
        }

        @DisplayName("INTRO 타입이 존재하지 않을 때")
        @Test
        void whenGetIntroContent() {
            //given
            Pageable pageable = PageRequest.of(0, 5);
            //when
            Page<ContentDto> result = dataService.getContentDtos(INTRO, pageable);
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
            Ad content = saveContent(CONTENT, "content", "detail", "/root");
            for (int i = 0; i < 20; i++) {
                saveImage(content, "file.jpg", "zip.jpg");
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


    @DisplayName("컨텐츠를 변경된다.")
    @Test
    public void updateContent() {
        //given
        long id = saveContent(CONTENT, "before", "before detail", "/root").getId();
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

    private static Image createImage(Ad content, String image, String compressImage) {
        return Image.builder().content(content).imageName(image).compressImageName(compressImage).priority(1).build();
    }

    private Ad createContent(String tittle, AdType type, String detail) {
        return Ad.builder().title(tittle).detail(detail).type(type).resourcePath("/path").build();
    }

    private Image saveImage(Ad content, String originalName, String compressName) {
        return imageRepository.saveAndFlush(Image.builder()
                .imageName(originalName)
                .content(content)
                .compressImageName(compressName)
                .build());
    }

    private Ad saveContent(AdType adType, String title, String detail, String resourcePath) {
        return adRepository.saveAndFlush(Ad.builder()
                .type(adType)
                .title(title)
                .detail(detail)
                .resourcePath(resourcePath)
                .build());
    }
//
//    @Test
//    public void amendIntro() {
//        IntroDto introDto = new IntroDto();
//        introDto.setComments(List.of("test", "test1", UUID.randomUUID().toString()));
//        introDto.setImage("new Image");
//
//        dataService.amendIntro(introDto);
//
//        IntroDto findIntroDto = dataService.getIntroDto();
//
//        List<String> comments = findIntroDto.getComments();
//        log.info("set : "  + introDto.getImage());
//        log.info("find : " + findIntroDto.getImage());
//        for (String comment : comments) {
//            assertThat(introDto.getComments().contains(comment)).isTrue();
//        }
//        assertThat(findIntroDto.getImage().contains(introDto.getImage())).isTrue();
//    }
//
//    @Test
//    public void deleteAd() throws IOException {
//        List<ContentDto> dtos = dataService.getContentDtos();
//
//        int size = dtos.size();
//        ContentDto dto = dtos.get(0);
//
//        dataService.deleteAd(Long.parseLong(dto.getId()));
//
//        assertThat(size).isSameAs(dataService.getContentDtos().size()+1);
//
//        dataService.createContentAd(dto);
//    }
}