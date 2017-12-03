public abstract class Field {

    protected Pawn pawn = null;
    protected Color color = null;

    public boolean isOccupied() {
        if(pawn != null)
            return true;
        else
            return false;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public Color getColor() { return color; }
}
