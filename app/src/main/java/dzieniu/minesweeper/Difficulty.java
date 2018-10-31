package dzieniu.minesweeper;

public enum Difficulty {

    BEGINNER,
    EASY,
    INTERMEDIATE,
    EXPERT,
    CUSTOM;

    public static Difficulty difficultyOf(int width, int height, int mines) {

        if(height==9 && width==9 && mines==10) {
            return BEGINNER;
        }else if(height==12 && width==12 && mines==24) {
            return EASY;
        }else if(height==16 && width==16 && mines==40) {
            return INTERMEDIATE;
        }else if(height==30 && width==16 && mines==99) {
            return EXPERT;
        } else return CUSTOM;
    }
}
