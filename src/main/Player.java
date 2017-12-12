public class Player {

    private Color color;
    private String name;

    public Player(Color playerColor, String playerName) {
        this.color = playerColor;
        this.name = playerName;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
