package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.service.business.DataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Rollback
class DataServiceTest {

    @MockBean
    AdRepository adRepository;
    @Autowired
    DataService dataService;

    @Value("${spring.url.image}")
    private String imageUrl;

    @DisplayName("표지 화면에 필요한 정보를 반환한다.")
    @Test
    public void mainPageData() {
        // given
        Ad content = createContent("this is title", AdType.INTRO, "hello world");
        content.addImage(createImage(content, "test.jpg", "compress.jpg"));
        new IntroDto("this is title", "/test.jpg", "hello world");
        given(adRepository.findByType(eq(AdType.INTRO)))
                .willReturn(content);
        // when
        IntroDto mainPageData = dataService.mainPageData();
        // then
        assertThat(mainPageData)
                .extracting("title", "imageUrl", "description")
                .containsExactly("this is title", imageUrl + content.getResourcePath() + "/test.jpg", "hello world");
    }

    private static Image createImage(Ad content, String image, String compressImage) {
        return Image.builder().content(content).imageName(image).compressImageName(compressImage).priority(1).build();
    }

    private Ad createContent(String tittle, AdType type, String detail) {
        return Ad.builder().title(tittle).detail(detail).type(type).resourcePath("/path").build();
    }
//    @Autowired
//    DataService dataService;
//    @Autowired
//    ImageRepository imageRepository;
//    @Autowired
//    AdRepository adRepository;
//
//    static ContentDto contentDto;
//
//    @BeforeEach
//    public void createData() throws IOException {
//        log.info("create Data start");
//        log.info("create intro");
//        //create intro ad
//        dataService.createIntroAd();
//        log.info("create content");
//        //create content ad
//        contentDto = new ContentDto();
//        contentDto.addImage("image1" + UUID.randomUUID());
//        contentDto.addImage("image2" + UUID.randomUUID());
//        contentDto.setTitle("create content ad " + UUID.randomUUID());
//        contentDto.setDescription("create content ad test detail" + UUID.randomUUID());
//
//        dataService.createContentAd(contentDto);
//        log.info("test method start");
//    }
//
//    @AfterEach
//    public void deleteData(){
//        log.info("delete data start");
//        imageRepository.deleteAllInBatch();
//        adRepository.deleteAllInBatch();
//    }
//
//    @Test
//    public void getIntroDto(){
//        IntroDto introDto = dataService.getIntroDto();
//        assertThat(introDto).isNotNull();
//    }
//
//    @Test
//    public void getContentDtos() throws IOException {
//
//        List<ContentDto> contents = dataService.getContentDtos().stream()
//                .filter(content ->
//                        contentDto.getTitle().equals(content.getTitle()) &&
//                        contentDto.getDescription().equals(content.getDescription()) &&
//                        contentDto.getImages().size() == content.getImages().size() )
//                .collect(Collectors.toList());
//
//        assertThat(contents.isEmpty()).isFalse();
//        assertThat(contents.size()).isOne();
//    }
//
//    @Test
//    public void getImageSrcByContentId() throws IOException {
//        List<ContentDto> contentDtos = dataService.getContentDtos();
//        for (ContentDto dto : contentDtos) {
//            List<String> imageUrls = dataService.getCompressImageSrcByContentId(dto.getId(), 0, 100);
//            for (String imageUrl : dto.getImages()) {
//                assertThat(imageUrls.contains(imageUrl)).isTrue();
//            }
//        }
//    }
//
//    @Test
//    public void amendContent() throws IOException {
//        List<ContentDto> dtos = dataService.getContentDtos();
//        ContentDto before = dtos.get(0);
//        before.setDescription("change description : " + UUID.randomUUID());
//        before.setTitle("change title : " + UUID.randomUUID());
//
//        dataService.amendContent(before);
//        ContentDto after = dataService.getContentDtoById(Long.parseLong(before.getId())).get();
//
//        assertThat(before.getDescription()).isEqualTo(after.getDescription());
//        assertThat(before.getTitle()).isEqualTo(after.getTitle());
//    }
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