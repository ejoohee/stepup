package com.pi.stepup.domain.music.dao;

import com.pi.stepup.domain.music.domain.MusicApply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MusicApplyRepositoryImpl implements MusicApplyRepository {
    private final EntityManager em;

    @Override
    public MusicApply insert(MusicApply musicApply) {
        em.persist(musicApply);
        return musicApply;
    }

    @Override
    public List<MusicApply> findAll(String keyword) {
        String sql = "SELECT ma FROM MusicApply ma ";

        if (StringUtils.hasText(keyword) && !keyword.equals("")) {
            sql += "WHERE ma.title LIKE concat('%', " + keyword + ", '%') OR " +
                    "ma.artist LIKE concat('%', " + keyword + ", '%')";
        }

        return em.createQuery(sql, MusicApply.class).getResultList();
    }

    @Override
    public Optional<MusicApply> findOne(Long musicApplyId) {
        Optional<MusicApply> musicApply = null;

        try {
            musicApply = Optional.ofNullable(em.find(MusicApply.class, musicApplyId));
        } catch (NoResultException e) {
            musicApply = Optional.empty();
        }
        return musicApply;
    }
}
