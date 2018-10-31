package dzieniu.minesweeper;

import android.widget.Button;

public class Field {

    private Button button;
    private String content;

    private Field
            NW,N,NE,
            W,    E,
            SW,S,SE;

    private int clicked;

    public Field(Button button, String content){
        this.button = button;
        this.content = content;
        this.clicked = 0;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public Button getButton(){
        return this.button;
    }

    public int getClicked(){
        return this.clicked;
    }

    public void setClicked(int clicked){
        this.clicked = clicked;
    }

    public void setNW(Field NW) {
        this.NW = NW;
    }

    public void setN(Field n) {
        N = n;
    }

    public void setNE(Field NE) {
        this.NE = NE;
    }

    public void setW(Field w) {
        W = w;
    }

    public void setE(Field e) {
        E = e;
    }

    public void setSW(Field SW) {
        this.SW = SW;
    }

    public void setS(Field s) {
        S = s;
    }

    public void setSE(Field SE) {
        this.SE = SE;
    }

    public Field getNW(){
        return this.NW;
    }

    public Field getN(){
        return this.N;
    }

    public Field getNE(){
        return this.NE;
    }

    public Field getW(){
        return this.W;
    }

    public Field getE(){
        return this.E;
    }

    public Field getSW(){
        return this.SW;
    }

    public Field getS(){
        return this.S;
    }

    public Field getSE(){
        return this.SE;
    }
}
