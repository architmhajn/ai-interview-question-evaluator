package model;

public class EvaluationResult {

    private double similarity;
    private int score;
    private String feedback;

    public double getSimilarity() { return similarity; }
    public int getScore() { return score; }
    public String getFeedback() { return feedback; }

    public void setSimilarity(double similarity) { this.similarity = similarity; }
    public void setScore(int score) { this.score = score; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
