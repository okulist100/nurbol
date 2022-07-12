package kg.mega.university.repos;

import kg.mega.university.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);
    Optional<Message> findById(Integer id);

}
