package com.promotion.handwriting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Rollback
class DataServiceTest {

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