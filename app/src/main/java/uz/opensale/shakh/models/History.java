package uz.opensale.shakh.models;

public class History {
    private int id;
    private int user_id;
    private int card_id;
    private int terminal_id;
    private int uzs;
    private String created_at;
    private String username;
    private String card_number;
    private String terminal_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getTerminal_name() {
        return terminal_name;
    }

    public void setTerminal_name(String terminal_name) {
        this.terminal_name = terminal_name;
    }

    public History(int id, int user_id, int card_id, int terminal_id, int uzs, String created_at, String username, String card_number, String terminal_name){
        this.id = id;
        this.user_id = user_id;
        this.card_id = card_id;
        this.terminal_id = terminal_id;
        this.uzs = uzs;
        this.created_at = created_at;
        this.username = username;
        this.card_number = card_number;
        this.terminal_name = terminal_name;

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(int terminal_id) {
        this.terminal_id = terminal_id;
    }

    public int getUzs() {
        return uzs;
    }

    public void setUzs(int uzs) {
        this.uzs = uzs;
    }
}
