-- 메인 페이지 초기 데이터
call next value for hibernate_sequence;
insert into CONTENTS (`id`, `create_time`, `modify_time`, `ad_type`, `title`, `detail`) VALUES (1, now(), now(),'INTRO', 'init intro', 'init detail');
-- 초기 관리자 데이터
call next value for hibernate_sequence;
insert into USERS (`id`, `create_time`, `modify_time`, `user_Id`, `password`, `role`) VALUES (1, now(), now(),'admin', '$2a$10$HYEOIGZNEwHuremqc9idXeeaOYs6wNdunjuFPgvPLkBD7NXsbM1uu', 'ADMIN');
