package com.pi.stepup.domain.music.service;

import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.MUSIC_APPLY_NOT_FOUND;
import static com.pi.stepup.domain.music.constant.MusicExceptionMessage.UNAUTHORIZED_USER_ACCESS;
import static com.pi.stepup.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND;
import static com.pi.stepup.global.config.security.SecurityUtils.getLoggedInUserId;

import com.pi.stepup.domain.music.dao.MusicApplyRepository;
import com.pi.stepup.domain.music.domain.Heart;
import com.pi.stepup.domain.music.domain.MusicApply;
import com.pi.stepup.domain.music.dto.MusicRequestDto.HeartSaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicRequestDto.MusicApplySaveRequestDto;
import com.pi.stepup.domain.music.dto.MusicResponseDto.MusicApplyFindResponseDto;
import com.pi.stepup.domain.music.exception.MusicApplyNotFoundException;
import com.pi.stepup.domain.music.exception.UnauthorizedUserAccessException;
import com.pi.stepup.domain.user.dao.UserRepository;
import com.pi.stepup.domain.user.domain.User;
import com.pi.stepup.domain.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MusicApplyServiceImpl implements MusicApplyService {

    private final MusicApplyRepository musicApplyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void create(MusicApplySaveRequestDto musicApplySaveRequestDto) {
        String id = getLoggedInUserId();
        User writer = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException(USER_NOT_FOUND.getMessage())
        );

        MusicApply musicApply = musicApplySaveRequestDto.toEntity(writer);
        musicApplyRepository.insert(musicApply);
    }

    @Override
    public List<MusicApplyFindResponseDto> readAllByKeyword(String keyword) {
        String id = getLoggedInUserId();
        List<MusicApply> musicApplies = musicApplyRepository.findAll(keyword, id);
        return setCanHeart(musicApplies);
    }

    @Override
    public List<MusicApplyFindResponseDto> readAllById() {
        String id = getLoggedInUserId();
        List<MusicApply> musicApplies = musicApplyRepository.findById(id);
        return setCanHeart(musicApplies);
    }

    public List<MusicApplyFindResponseDto> setCanHeart(List<MusicApply> musicApplies) {
        List<MusicApplyFindResponseDto> result = new ArrayList<>();

        for (MusicApply ma : musicApplies) {
            int canHeart = 1;
            if (ma.getHearts().size() != 0) {
                canHeart = 0;
            }

            result.add(MusicApplyFindResponseDto.builder()
                .musicApply(ma)
                .canHeart(canHeart)
                .build());
        }
        return result;
    }

    @Override
    public MusicApplyFindResponseDto readOne(String id, Long musicApplyId) {
        return MusicApplyFindResponseDto.builder()
            .musicApply(musicApplyRepository.findOne(musicApplyId)
                .orElseThrow(
                    () -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()))
            )
            .canHeart(findHeartStatus(musicApplyId))
            .build();
    }

    @Override
    @Transactional
    public void delete(Long musicApplyId) {
        String id = getLoggedInUserId();

        MusicApply musicApply = musicApplyRepository.findOne(musicApplyId)
            .orElseThrow(
                () -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage())
            );

        if (id.equals(musicApply.getWriter().getId())) {
            musicApplyRepository.delete(musicApplyId);
        } else {
            throw new UnauthorizedUserAccessException(UNAUTHORIZED_USER_ACCESS.getMessage());
        }
    }

    @Override
    @Transactional
    public void createHeart(HeartSaveRequestDto heartSaveRequestDto) {
        String id = getLoggedInUserId();
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.getMessage()));
        MusicApply musicApply = musicApplyRepository.findOne(heartSaveRequestDto.getMusicApplyId())
            .orElseThrow(() -> new MusicApplyNotFoundException(MUSIC_APPLY_NOT_FOUND.getMessage()));
        Heart heart = heartSaveRequestDto.toEntity(user, musicApply);

        musicApplyRepository.insert(heart);

        // TODO : Entity 안에 PostPersist, PostRemove
        musicApply.addHeart();
    }

    @Override
    @Transactional
    public void deleteHeart(Long musicRequestId) {
        String id = getLoggedInUserId();
        Optional<Heart> heart = musicApplyRepository.findHeart(id, musicRequestId);

        if (heart.isPresent()) {
            musicApplyRepository.deleteHeart(heart.get().getHeartId());
            MusicApply musicApply = heart.get().getMusicApply();
            musicApply.removeHeart();
        }
    }

    @Override
    public Integer findHeartStatus(Long musicApplyId) {
        String id = getLoggedInUserId();
        Optional<Heart> heart = musicApplyRepository.findHeart(id, musicApplyId);

        if (heart.isPresent()) {
            return 0;
        } else {
            return 1;
        }
    }
}
