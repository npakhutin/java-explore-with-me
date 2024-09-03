package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(List<Long> idList, PageRequest pageable);

    @Query(value = """
            select subscriber
            from User subscriber
                join subscriber.subscriptions eventsInitiator
            where eventsInitiator = ?1
            """)
    List<User> findAllSubscribers(User eventsInitiator);
}
