package model;

public class RecordData {
    public int game_id;
    public int turn;
    public String player; //b or w
    // 规定
    //    pos_before == pos_after, pos_after == pos_affect : 普通放置
    //    pos_before == pos_after, pos_after != pos_affect : 放置时吃子
    //    pos_before != pos_after, pos_after == pos_affect : 普通移动
    //    pos_before != pos_after, pos_after != pos_affect : 移动后吃子
    public int pos_before;
    public int pos_after;
    public int pos_affect;

    public RecordData(int game_id, int turn, String player,
                      int pos_before, int pos_after, int pos_affect){
        this.game_id = game_id;
        this.turn = turn;
        this.player = player;
        this.pos_before = pos_before;
        this.pos_after = pos_after;
        this.pos_affect = pos_affect;
    }

    @Override
    public String toString() {
        return "RecordData{" +
                "game_id=" + game_id +
                ", turn=" + turn +
                ", player='" + player + '\'' +
                ", pos_before=" + pos_before +
                ", pos_after=" + pos_after +
                ", pos_affect=" + pos_affect +
                '}';
    }
}
