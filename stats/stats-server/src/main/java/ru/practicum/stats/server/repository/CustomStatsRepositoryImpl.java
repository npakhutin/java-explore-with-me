package ru.practicum.stats.server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.Hit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class CustomStatsRepositoryImpl implements CustomStatsRepository {
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<StatsDto> stats(LocalDateTime start, LocalDateTime end, List<String> uriList, boolean unique) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StatsDto> q = cb.createQuery(StatsDto.class);

        Root<Hit> h = q.from(Hit.class);
        if (unique) {
            q.multiselect(cb.construct(StatsDto.class,
                                       h.get(Hit.Fields.app),
                                       h.get(Hit.Fields.uri),
                                       cb.countDistinct(h.get(Hit.Fields.ip))));
        } else {
            q.multiselect(cb.construct(StatsDto.class,
                                       h.get(Hit.Fields.app),
                                       h.get(Hit.Fields.uri),
                                       cb.count(h.get(Hit.Fields.ip))));
        }

        List<Predicate> predicates = new ArrayList<>();
        if (start != null) {
            predicates.add(cb.greaterThanOrEqualTo(h.get(Hit.Fields.timestamp), start));
        }
        if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(h.get(Hit.Fields.timestamp), end));
        }
        if (uriList != null && !uriList.isEmpty()) {
            predicates.add(h.get(Hit.Fields.uri).in(uriList));
        }

        q.where(predicates.toArray(new Predicate[]{}));
        q.groupBy(h.get(Hit.Fields.app), h.get(Hit.Fields.uri));
        q.orderBy(cb.desc(cb.count(h.get(Hit.Fields.ip))));

        return em.createQuery(q).getResultList();
    }
}
