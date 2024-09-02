package ru.practicum.ewm.compilation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.practicum.ewm.event.model.Event;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @ManyToMany
    @JoinTable(name = "event_compilation",
               joinColumns = {@JoinColumn(name = "compilation_id")},
               inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events = new ArrayList<>();
}
