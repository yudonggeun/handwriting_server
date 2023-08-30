package com.promotion.handwriting.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FileServiceTest {

//    @Autowired
//    FileService fileService;
//    @Autowired
//    AdRepository adRepository;
//    @Autowired
//    ResourceLoader loader;
//    @Autowired
//    DataController dataController;
//    @Autowired
//    DataService dataService;
//
//    Ad ad;
//    ClassPathResource resource = new ClassPathResource("static/image/no_image.jpg");
//    MockMultipartFile file = new MockMultipartFile("image",
//            "test.png",
//            "image/png",
//            new FileInputStream(resource.getFile()));
//
//    FileServiceTest() throws IOException {
//    }
//
//    @BeforeEach
//    void inputData() throws IOException {
//        ad = Ad.createAd(AdType.INTRO, "", "소개입니다.", "/test_file");
//        adRepository.save(ad);
//    }
//
//    @AfterEach
//    void deleteData() {
//        if (ad != null)
//            adRepository.delete(ad);
//    }
//
//    @Test
//    void saveIntroFile() throws IOException {
//        fileService.saveIntroFile(file);
//        String resourcePath = dataService.getResourcePathOfAd(ad.getId());
//        fileService.deleteFile(file.getOriginalFilename(), resourcePath);
//
//        String imageResourcePath = FileUtil.getImageResourcePath();
//        Resource resource = loader.getResource(imageResourcePath + ad.getResourcePath());
//        Path path = resource.getFile().toPath();
//        File deleteFile = path.resolve(this.file.getOriginalFilename()).toFile();
//        assertThat(deleteFile.exists()).isFalse();
//    }
//
//    @Test
//    void saveFileAndDeleteFile() throws IOException {
//        String resourcePath = dataService.getResourcePathOfAd(ad.getId());
//        fileService.saveContentFile(file, resourcePath);
//        fileService.deleteFile(file.getOriginalFilename(), resourcePath);
//
//        String imageResourcePath = FileUtil.getImageResourcePath();
//        Resource resource = loader.getResource(imageResourcePath + ad.getResourcePath());
//        Path path = resource.getFile().toPath();
//        File deleteFile = path.resolve(this.file.getOriginalFilename()).toFile();
//        assertThat(deleteFile.exists()).isFalse();
//    }
//
//    @Test
//    void deleteFile() throws IOException {
//
//        //save File
//        Ad contentAd = Ad.createAd(AdType.CONTENT, "test Content", "소개입니다.", "/test_file");
//        contentAd = adRepository.save(contentAd);
//        String resourcePath = dataService.getResourcePathOfAd(ad.getId());
//        fileService.saveContentFile(file, resourcePath);
//
//        //delete File
//        List<String> fileList = new LinkedList<>();
//        fileList.add(file.getOriginalFilename());
//        fileService.deleteFiles(fileList, resourcePath);
//        //assert
//        String imageResourcePath = FileUtil.getImageResourcePath();
//        Resource resource = loader.getResource(imageResourcePath + contentAd.getResourcePath());
//        Path path = resource.getFile().toPath();
//        File deletedFile = path.resolve(file.getOriginalFilename()).toFile();
//        assertThat(deletedFile.exists()).isFalse();
//    }
}