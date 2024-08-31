package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.User;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiator(User initiator, PageRequest pageable);

    Optional<Event> findByIdAndState(Long eventId, EventState state);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Modifying
    @Query(value = """
            update Event e set e.confirmedRequests = (
                select count(r.id) from ParticipationRequest r where r.event = e
                    and r.status = 'CONFIRMED'
            )
            where e = ?1
            """)
    int updateEventConfirmedRequests(Event event);
}
