import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum  ProfileType {
    GUEST("guest"),
    USER("user"),
    MODERATOR("moder"),
    ADMIN("admin");

    private final String restName;
}
