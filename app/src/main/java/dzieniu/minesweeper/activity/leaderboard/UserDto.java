package dzieniu.minesweeper.activity.leaderboard;

public class UserDto {

    private String displayName;

    private Long time;

    public UserDto() {
    }

    public UserDto(String displayName, Long time) {
        this.displayName = displayName;
        this.time = time;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
