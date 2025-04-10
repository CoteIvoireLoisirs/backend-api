package ca.deltagis.success.v1.infrastructure.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.deltagis.success.v1.domain.core.entities.project.Tag;


public interface TagRepository extends JpaRepository<Tag, Long> {
}
