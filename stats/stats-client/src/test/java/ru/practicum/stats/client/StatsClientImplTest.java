package ru.practicum.stats.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsClientImplTest {
    private final StatsClient client;

//    @Test
//    void hit() {
//        HitDto hitDto = client.hit("test.app", "/test/endpoint", "192.168.1.1");
//        assertThat(hitDto.getApp(), equalTo("test.app"));
//    }
//
//    @Test
//    void stats() {
//        HitDto hitDto = client.hit("test.app", "/test/endpoint", "192.168.1.1");
//        List<StatsDto> statsDto = client.stats(LocalDateTime.now().minusSeconds(1),
//                                               LocalDateTime.now().plusSeconds(1),
//                                               null,
//                                               true);
//        assertThat(statsDto.size(), not(0));
//    }
}