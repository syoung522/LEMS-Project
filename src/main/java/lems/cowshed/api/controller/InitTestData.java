package lems.cowshed.api.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lems.cowshed.domain.user.Gender;
import lems.cowshed.domain.user.Mbti;
import lems.cowshed.domain.user.User;
import lems.cowshed.domain.user.UserRepository;
import lems.cowshed.domain.userevent.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitTestData {

    private final InitUserService initUserService;

    @PostConstruct
    public void init() {
        initUserService.init();
    }

    @Component
    static class InitUserService{
        @Autowired
        private EntityManager em;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Transactional
        public void init() {
            for(int i = 1; i <= 3; i++) {
                User user = new User(null,"테스트" + i,
                        bCryptPasswordEncoder.encode("1234"),
                        "ROLE_USER", Gender.M,
                        "test" + i + "@naver.com",
                        LocalDate.parse("2000-01-01"),
                        "대구광역시 동구", Mbti.ENFJ,
                        "안녕하세요",
                        LocalDateTime.now(), LocalDateTime.now());
                em.persist(user);

                UserEvent userEvent = new UserEvent();
                userEvent.relationSetter(user, null);
                em.persist(userEvent);
            }
        }
    }
}
