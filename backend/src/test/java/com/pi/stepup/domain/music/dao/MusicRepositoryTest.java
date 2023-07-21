package com.pi.stepup.domain.music.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.pi.stepup.domain.music.domain.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MusicRepositoryTest {

    @Autowired
    private MusicRepository musicRepository;
    private Music music;

    @Test
    @BeforeEach
    public void init() {
        music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
    }

    @Test
    @DisplayName("MusicRepository가 null이 아님을 테스트")
    public void musicRepositoryNotNullTest() {
        assertThat(musicRepository).isNotNull();
    }

    @Test
    @DisplayName("노래 추가 테스트")
    @Transactional
    public void insertMusicRepoTest() {

        Music result = musicRepository.insert(music);
        assertThat(music).isEqualTo(result);
    }

    @Test
    @DisplayName("노래 한 곡 조회 테스트")
    @Transactional
    public void findOneMusicRepoTest() {
        Music music = Music.builder()
            .title("spicy")
            .artist("aespa")
            .answer("")
            .URL("url")
            .build();
        musicRepository.insert(music);

        Long musicId = music.getMusicId();
        Music result = musicRepository.findOne(musicId);
        assertThat(music.getTitle()).isEqualTo(result.getTitle());
    }

    @Test
    @DisplayName("노래 전체 목록 조회 테스트")
    @Transactional
    public void findAllMusicRepoTest(){

    }

    @Test
    @DisplayName("노래 전체 목록 키워드 조회 테스트")
    @Transactional
    public void findAllMusicByKeywordRepoTest(){

    }
}