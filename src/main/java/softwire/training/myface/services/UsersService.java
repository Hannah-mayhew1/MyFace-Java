package softwire.training.myface.services;

import org.jdbi.v3.core.Handle;
import org.springframework.stereotype.Service;
import softwire.training.myface.models.dbmodels.User;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService extends DatabaseService {

    public List<String> guessAllUsernames() {
        try (Handle handle = jdbi.open()) {
            return handle
                    .createQuery("(SELECT DISTINCT UserName FROM Users)")
                    .mapTo(String.class)
                    .list();
        }
    }

    public Optional<User> getUserWithUserName(String userName) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM users WHERE UserName = :UserName")
                    .bind("UserName", userName)
                    .mapToBean(User.class)
                    .findFirst()
        );
    }

    public Optional<String> getUserPassword (String userName) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT PasswordPlainText FROM Users WHERE UserName = :UserName")
                    .bind("UserName", userName)
                    .mapTo(String.class)
                    .findFirst()
        );
    }

    public void addUser(User user) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO users (FullName, UserName, PasswordPlainText) VALUES (:FullName, :UserName, :PasswordPlainText)")
                        .bind("FullName", user.getFullName())
                        .bind("UserName", user.getUserName())
                        .bind("PasswordPlainText", user.getPasswordPlainText())
                        .execute()
        );
    }

}
