package model;

public class Player {
    private final Person person;
    private final PlayerType type;
    private final BattingScoreCard battingScoreCard;
    private final BowlingScoreCard bowlingScoreCard;

    public Player(Person person, PlayerType type) {
        this.person = person;
        this.type = type;
        this.battingScoreCard = new BattingScoreCard();
        this.bowlingScoreCard = new BowlingScoreCard();
    }

    public Person getPerson() {
        return person;
    }

    public PlayerType getType() {
        return type;
    }

    public BattingScoreCard getBattingScoreCard() {
        return battingScoreCard;
    }

    public BowlingScoreCard getBowlingScoreCard() {
        return bowlingScoreCard;
    }

    public String getName() {
        return person != null ? person.getName() : "Unknown";
    }
}
