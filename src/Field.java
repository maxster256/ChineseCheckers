public abstract class Field {

    protected Pawn pawn;

    public Boolean isOccupied() {

        // TODO: implement
        return true;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
